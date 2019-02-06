**PayBox SDK (Android)**


PayBox SDK Android - это библиотека позволяющая упростить взаимодействие с API PayBox. Система SDK работает на Android 4.4 и выше

**Описание возможностей:**

- Инициализация платежа
- Отмена платежа
- Возврат платежа
- Проведение рекуррентного платежа с сохраненными картами
- Получение информации/статуса платежа
- Добавление карт
- Оплата добавленными картами
- Удаление карт


**Установка:**

1. Добавьте репозитории Jitpack в ваш build.gradle в конец репозиториев:
```
repositories {
    // ...
    maven { url "https://jitpack.io" }
}
```
<br><br>
2. Добавьте зависимость:
```
dependencies {
    implementation 'com.github.PayBox:SDK_Android_input:1.0.3.5@aar'
}
```
**Инициализация SDK:**
```
        PBHelper.Builder builder = new PBHelper.Builder(appContext,secretKey,merchantId);
```


Выбор платежной системы:
```
        builder.setPaymentSystem(Constants.PBPAYMENT_SYSTEM.EPAYWEBKZT);
```


Выбор валюты платежа:
```
        builder.setPaymentCurrency(Constants.CURRENCY.KZT);
```


Дополнительная информация пользователя, если не указано, то выбор будет предложен на сайте платежного гейта:
```
        builder.setUserInfo(email, 8777*******);
```


Активация автоклиринга:
```
        builder.enabledAutoClearing(true);
```

Для активации режима тестирования:
```
        builder.enabledTestMode(true);
```


Для передачи информации от платежного гейта:
```
        builder.setFeedBackUrl(checkUrl,resultUrl,refundUrl,captureUrl, REQUEST_METHOD);
```

Время (в секундах) в течение которого платеж должен быть завершен, в противном случае, при проведении платежа, PayBox откажет платежной системе в проведении (мин. 300 (5 минут), макс. 604800 (7 суток), по умолчанию 300):
```
        builder.setPaymentLifeTime(300);
```


**Инициализация параметров:**
```
        builder.build();
```

**Работа с SDK:**


Для связи с SDK, имплементируйте в Activity -> “PBListener”:
1. В методе onCreate() добавьте:
```
        PBHelper.getSdk().registerPbListener(this);
```
2. В методе onDestroy():
```
        PBHelper.getSdk().removePbListener(this);
```

**Для инициализации платежа** (при инициализации с параметром .enableRecurring(int) и передачей userId, карты сохраняются в системе PayBox):
```
        PBHelper.getSdk().initNewPayment(orderId, userId, amount, description, extraParams);
```
В ответ откроется webView для заполнения карточных данных, после успешной оплаты вызовется метод:
```
        public void onPaymentPaid(Response response)
```
Активация режима рекуррентного платежа: во входном параметре указывается время, на протяжении которого продавец рассчитывает использовать профиль рекуррентных платежей. Минимальное допустимое значение 1 (1 месяц). Максимальное допустимое значение: 156 (13 лет):
```
        PBHelper.getSdk().enableRecurring(3);
```
Отключение режима рекуррентного платежа:
```
        PBHelper.getSdk().disableRecurring();
```


**Для отмены платежа, по которому не прошел клиринг:**
```
        PBHelper.getSdk().initCancelPayment(paymentId);
```
После успешной операции вызовется метод:
```
        public void onPaymentCanceled(Response response)
```


**Для проведения возврата платежа, по которому прошел клиринг:**
```
        PBHelper.getSdk().initRevokePayment(paymentId, amount);
```
После успешной операции вызовется метод:
```
        public void onPaymentRevoke(Response response)
```


**Для проведения рекуррентного платежа добавленной картой:**
```
        PBHelper.getSdk().makeRecurringPayment(amount, orderId, recurringProfileId, description, extraParams);
```
После успешной операции вызовется метод:
```
        public void onRecurringPaid(RecurringPaid recurringPaid)
```


**Для получения статуса платежа:**
```
        PBHelper.getSdk().getPaymentStatus(paymentId);
```
После успешной операции вызовется метод:
```
        public void onPaymentStatus(PStatus pStatus)
```


**Для проведения клиринга:**
```
        PBHelper.getSdk().initPaymentDoCapture(paymentId);
```
После успешной операции вызовется метод:
```
        public void onPaymentCaptured(Capture capture)
```


**Для добавления карты:**
```
        PBHelper.getSdk().addCard(userId, postUrl); //postUrl - для обратной связи
```
В ответ откроется &quot;webView&quot; для заполнения карточных данных, после успешной операции вызовется метод:
```
        public void onCardAdded(Response response)
```


**Для удаления карт:**
```
        PBHelper.getSdk().removeCard(userId, cardId);
```
После успешной операции вызовется метод:
```
        public void onCardRemoved(Card card)
```


**Для отображения списка карт:**
```
        PBHelper.getSdk().getCards(userId);
```
После успешной операции вызовется метод:
```
        public void onCardListed(ArrayList&lt;Card&gt; cards)
```

**Для создания платежа добавленной картой:**
```
        PBHelper.getSdk().initCardPayment(amount, userId, cardId, orderId, description, extraParams);
```
После успешной операции вызовется метод:
```
        public void onCardPayInited(Response response)
```

**Для проведения платежа добавленной картой:**
```
        PBHelper.getSdk().payWithCard(paymentId); //paymentId
```
В ответ откроется &quot;webView&quot;, после успешной операции вызовется метод:
```
        public void onCardPaid(Response response)
        
```

**Описание некоторых входных параметров**

1. orderId - Идентификатор платежа в системе продавца. Рекомендуется поддерживать уникальность этого поля.
2. amount - Сумма платежа
3. merchantId - Идентификатор продавца в системе PayBox. Выдается при подключении.
4. secretKey - Платежный пароль, используется для защиты данных, передаваемых системой PayBox магазину и магазином системе Paybox
5. userId - Идентификатор клиента в системе магазина продавца.
6. paymentId - Номер платежа сформированный в системе PayBox.
7. description - Описание товара или услуги. Отображается покупателю в процессе платежа.
8. extraParams - Дополнительные параметры продавца. Имена дополнительных параметров продавца должны быть уникальными. 
9. checkUrl - URL для проверки возможности платежа. Вызывается перед платежом, если платежная система предоставляет такую возможность. Если параметр не указан, то берется из настроек магазина. Если параметр установлен равным пустой строке, то проверка возможности платежа не производится.                                                                                                                                       возможности платежа не производится.
10. resultUrl - URL для сообщения о результате платежа. Вызывается после платежа в случае успеха или неудачи. Если параметр не указан, то берется из настроек магазина. Если параметр установлен равным пустой строке, то PayBox не сообщает магазину о результате платежа.
11. refundUrl - URL для сообщения об отмене платежа. Вызывается после платежа в случае отмены платежа на стороне PayBoxа или ПС. Если параметр не указан, то берется из настроек магазина.
12. captureUrl - URL для сообщения о проведении клиринга платежа по банковской карте. Если параметр не указан, то берется из настроек магазина.
13. REQUEST_METHOD - GET, POST или XML – метод вызова скриптов магазина checkUrl, resultUrl, refundUrl, captureUrl для передачи информации от платежного гейта.