package org.home.tracker.service

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.home.tracker.ExpenseTrackerApplication
import org.home.tracker.dto.CategoryDto
import org.home.tracker.dto.ExpenseDto
import org.home.tracker.dto.ExpenseRuleDto
import org.home.tracker.persistence.repository.CategoryRepository
import org.home.tracker.persistence.repository.ExpenseRepository
import org.home.tracker.persistence.repository.ExpenseRuleRepository
import org.home.tracker.persistence.repository.SharedDataRepository

class BankNotificationListenerService : NotificationListenerService() {

    private var componentName: ComponentName? = null

    private lateinit var expenseRepository: ExpenseRepository

    private lateinit var categoryRepository: CategoryRepository

    private lateinit var expenseRuleRepository: ExpenseRuleRepository

    private lateinit var handlers: Map<String, NotificationHandler>

    override fun onCreate() {
        super.onCreate()

        expenseRepository = (application as ExpenseTrackerApplication).container.expenseRepository
        categoryRepository = (application as ExpenseTrackerApplication).container.categoryRepository
        expenseRuleRepository = (application as ExpenseTrackerApplication).container.expenseRuleRepository

        handlers = mapOf(
            "ru.vtb24.mobilebanking.android" to VtbNotificationHandler(
                categoryRepository,
                expenseRepository,
                expenseRuleRepository
            )
        )
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        val packageName = sbn?.packageName ?: ""

        val extras = sbn?.notification?.extras

        val title = extras?.getCharSequence("android.title").toString()
        val text = extras?.getCharSequence("android.text").toString()

        val handler = handlers[packageName] ?: return

        if (handler.check(text)) {
            handler.handle(title, text)
        }
    }

    interface NotificationHandler {

        fun check(text: String): Boolean

        fun handle(title: String, text: String)
    }

    abstract class AbstractNotificationHandler : NotificationHandler {

        protected fun parseDouble(text: String): Double {
            return text.replace(" ", "")
                .replace(",", ".")
                .toDouble()
        }

        protected fun createExpense(cost: Double, category: CategoryDto): ExpenseDto {
            return ExpenseDto(
                id = 0L,
                date = System.currentTimeMillis(),
                value = (cost * 100).toLong(),
                category = category,
                currencyId = "RUB"
            )
        }

    }

    class VtbNotificationHandler(
        private val categoryRepository: CategoryRepository,
        private val expenseRepository: ExpenseRepository,
        private val expenseRuleRepository: ExpenseRuleRepository
    ) : AbstractNotificationHandler() {

        private val checkString = "Покупка"

        override fun check(text: String): Boolean {
            return text.startsWith(checkString)
        }

        override fun handle(title: String, text: String) {
            val endIndex = text.indexOf("₽")
            val cost = parseDouble(text.substring(checkString.length, endIndex).trim())

            MainScope().launch {
                var category = categoryRepository.findAll().find { it.name == "Прочее" }
                if (category == null) {
                    category = categoryRepository.save(CategoryDto(id = null, "Прочее"))
                }

                val rules = expenseRuleRepository.findAll().associateBy(ExpenseRuleDto::pattern)
                val key = rules.keys.find { title.contains(it) }
                if (key != null) {
                    category = rules.getValue(key).category
                }

                expenseRepository.save(createExpense(cost, category))
                SharedDataRepository.updateTime()
            }
        }

    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()

        if (componentName == null) {
            componentName = ComponentName(this, this::class.java)
        }

        componentName?.let { requestRebind(it) }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        if (componentName == null) {
            componentName = ComponentName(this, this::class.java)
        }

        componentName?.let {
            requestRebind(it)
            toggleNotificationListenerService(it)
        }

        return START_REDELIVER_INTENT
    }

    private fun toggleNotificationListenerService(componentName: ComponentName) {
        packageManager.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )

        packageManager.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

}