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

1. Скачиваете библиотеку [PayBoxSdk.aar](https://github.com/PayBox/SDK_Android_input/releases/download/1.0.1_input/payboxsdk.aar)
2. В Android Studio -&gt; File -&gt; New -&gt; New Module -&gt; Import .jar/.aar Package -&gt; Next, далее в поле File Name указываете путь к файлу
<br><br><br><br>
**Инициализация SDK:**

        PBHelper.Builder builder = new PBHelper.Builder(appContext,secretKey,merchantId);



Выбор платежной системы:

        builder.setPaymentSystem(Constants.PBPAYMENT\_SYSTEM._EPAYWEBKZT_);



Выбор валюты платежа:

        builder.setPaymentCurrency(Constants.CURRENCY._KZT_);



Дополнительная информация пользователя, если не указано, то выбор будет предложен на сайте платежного гейта:

        builder.setUserInfo(&quot;email&quot;,&quot;8777\*\*\*\*\*\*\*&quot;);



Активация автоклиринга:

        builder.enabledAutoClearing(true);


Для активации режима тестирования:

        builder.enabledTestMode(true);



Для передачи информации от платежного гейта:

        builder.setFeedBackUrl(&quot;checkUrl&quot;,&quot;resultUrl&quot;,&quot;refundUrl&quot;,&quot;captureUrl&quot;, REQUEST\_METHOD);



Время (в секундах) в течение которого платеж должен быть завершен, в противном случае, при проведении платежа, PayBox откажет платежной системе в проведении (мин. 300 (5 минут), макс. 604800 (7 суток), по умолчанию 300):

        builder.setPaymentLifeTime(300);







**Инициализация параметров:**

        builder.build();




**Работа с SDK:**


Для связи с SDK, имплементируйте в Activity -> “PBListener”:
1. В методе onCreate() добавьте:

        PBCard.getSdk().registerPbListener(this);

2. В методе onDestroy():

        PBCard.getSdk().removePbListener(this);


**Для инициализации платежа** (при инициализации с параметром &quot;PBHelper.getSdk().enableRecurring(int)&quot;, карты сохраняются в системе PayBox):

        PBHelper.getSdk().initNewPayment(orderId, userId, amount, description);

В ответ откроется &quot;webView&quot; для заполнения карточных данных, после успешной оплаты вызовется метод:

        public void onPaymentPaid(Response response)

Активация режима рекуррентного платежа: во входном параметре указывается время, на протяжении которого продавец рассчитывает использовать профиль рекуррентных платежей. Минимальное допустимое значение 1 (1 месяц). Максимальное допустимое значение: 156 (13 лет):

        PBHelper.getSdk().enableRecurring(3);



Отключение режима рекуррентного платежа:

        PBHelper.getSdk().disableRecurring();

**Для отмены платежа, по которому не прошел клиринг:**

        PBHelper.getSdk().initCancelPayment(paymentId);

После успешной операции вызовется метод:

        public void onPaymentCanceled(Response response)



**Для проведения возврата платежа, по которому прошел клиринг:**

        PBHelper.getSdk().initRevokePayment(paymentId, amount);

После успешной операции вызовется метод:

        public void onPaymentRevoke(Response response)



**Для проведения рекуррентного платежа добавленной картой:**

        PBHelper.getSdk().makeRecurringPayment(amount, orderId, recurringProfileId, description);

После успешной операции вызовется метод:

        public void onRecurringPaid(RecurringPaid recurringPaid)



**Для получения статуса платежа:**

        PBHelper.getSdk().getPaymentStatus(paymentId);

После успешной операции вызовется метод:

        public void onPaymentStatus(PStatus pStatus)



**Для проведения клиринга:**

        PBHelper.getSdk().initPaymentDoCapture(paymentId);

После успешной операции вызовется метод:

        public void onPaymentCaptured(Capture capture)



**Для добавления карты:**

        PBHelper.getSdk().addCard(userId, postUrl); //postUrl - для обратной связи

В ответ откроется &quot;webView&quot; для заполнения карточных данных, после успешной операции вызовется метод:

        public void onCardAdded(Response response)



**Для удаления карт:**

        PBHelper.getSdk().removeCard(userId, cardId);

После успешной операции вызовется метод:

        public void onCardRemoved(Card card)



**Для отображения списка карт:**

        PBHelper.getSdk().getCards(userId);

После успешной операции вызовется метод:

        public void onCardListed(ArrayList&lt;Card&gt; cards)



**Для создания платежа добавленной картой:**

        PBHelper.getSdk().initCardPayment(amount, userId, cardId, orderId, description);

После успешной операции вызовется метод:

        public void onCardPayInited(Response response)

**Для проведения платежа добавленной картой:**

        PBHelper.getSdk().payWithCard(paymentId); //paymentId

В ответ откроется &quot;webView&quot;, после успешной операции вызовется метод:

        public void onCardPaid(Response response)

