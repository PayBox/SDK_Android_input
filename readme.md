**PayBox SDK (Android)**


PayBox SDK Android - ��� ���������� ����������� ��������� �������������� � API PayBox. ������� SDK �������� �� Android 4.4 � ����

**�������� ������������:**

- ������������� �������
- ������ �������
- ������� �������
- ���������� ������������� ������� � ������������ �������
- ��������� ����������/������� �������
- ���������� ����
- ������ ������������ �������
- �������� ����


**���������:**

1. ���������� ���������� (PayBoxSdk.aar)
2. � Android Studio -&gt; File -&gt; New -&gt; New Module -&gt; Import .jar/.aar Package -&gt; Next, ����� � ���� File Name ���������� ���� � �����
<br><br><br><br>
**������������� SDK:**

        PBHelper.Builder builder = new PBHelper.Builder(appContext,secretKey,merchantId);



����� ��������� �������:

        builder.setPaymentSystem(Constants.PBPAYMENT\_SYSTEM._EPAYWEBKZT_);



����� ������ �������:

        builder.setPaymentCurrency(Constants.CURRENCY._KZT_);



�������������� ���������� ������������, ���� �� �������, �� ����� ����� ��������� �� ����� ���������� �����:

        builder.setUserInfo(&quot;email&quot;,&quot;8777\*\*\*\*\*\*\*&quot;);



��������� ������������:

        builder.enabledAutoClearing(true);



��������� ������ ������������� �������: �� ������� ��������� ����������� �����, �� ���������� �������� �������� ������������ ������������ ������� ������������ ��������. ����������� ���������� �������� 1 (1 �����). ������������ ���������� ��������: 156 (13 ���):

        builder.enableRecurring(3);



���������� ������ ������������� �������:

        builder.disableRecurring();



��� ��������� ������ ������������:

        builder.enabledTestMode(true);



��� �������� ���������� �� ���������� �����:

        builder.setFeedBackUrl(&quot;checkUrl&quot;,&quot;resultUrl&quot;,&quot;refundUrl&quot;,&quot;captureUrl&quot;, REQUEST\_METHOD);



����� (� ��������) � ������� �������� ������ ������ ���� ��������, � ��������� ������, ��� ���������� �������, PayBox ������� ��������� ������� � ���������� (���. 300 (5 �����), ����. 604800 (7 �����), �� ��������� 300):

        builder.setPaymentLifeTime(300);



��� ����� � SDK,  ��������������� � Activity -&gt; &quot;PBListener&quot;:

        builder.setPBListener(this)



**������������� ����������:**

        builder.build();




**������ � SDK:**



**��� ������������� �������** (��� ������������� � ���������� &quot;builder.enableRecurring(int)&quot;, ����� ����������� � ������� PayBox):

        PBHelper.getSdk().initNewPayment(orderId, userId, amount, description);

� ����� ��������� &quot;webView&quot; ��� ���������� ��������� ������, ����� �������� ������ ��������� �����:

        public void onPaymentPaid(Response response)



**��� ������ �������, �� �������� �� ������ �������:**

        PBHelper.getSdk().initCancelPayment(paymentId);

����� �������� �������� ��������� �����:

        public void onPaymentCanceled(Response response)



**��� ���������� �������� �������, �� �������� ������ �������:**

        PBHelper.getSdk().initRevokePayment(paymentId, amount);

����� �������� �������� ��������� �����:

        public void onPaymentRevoked(Response response)



**��� ���������� ������������� ������� ����������� ������:**

        PBHelper.getSdk().makeRecurringPayment(amount, orderId, recurringProfileId, description);

����� �������� �������� ��������� �����:

        public void onRecurringPaid(RecurringPaid recurringPaid)



**��� ��������� ������� �������:**

        PBHelper.getSdk().getPaymentStatus(paymentId);

����� �������� �������� ��������� �����:

        public void onPaymentStatus(PStatus pStatus)



**��� ���������� ��������:**

        PBHelper.getSdk().initPaymentDoCapture(paymentId);

����� �������� �������� ��������� �����:

        public void onPaymentCaptured(Capture capture)



**��� ���������� �����:**

        PBHelper.getSdk().addCard(userId, postUrl); //postUrl - ��� �������� �����

� ����� ��������� &quot;webView&quot; ��� ���������� ��������� ������, ����� �������� �������� ��������� �����:

        public void onCardAdded(Response response)



**��� �������� ����:**

        PBHelper.getSdk().removeCard(userId, cardId);

����� �������� �������� ��������� �����:

        public void onCardRemoved(Card card)



**��� ����������� ������ ����:**

        PBHelper.getSdk().getCards(userId);

����� �������� �������� ��������� �����:

        public void onCardListed(ArrayList&lt;Card&gt; cards)



**��� �������� ������� ����������� ������:**

        PBHelper.getSdk().initCardPayment(amount, userId, cardId, orderId, description);

����� �������� �������� ��������� �����:

        public void onCardPayInited(Response response)

**��� ���������� ������� ����������� ������:**

        PBHelper.getSdk().payWithCard(paymentId); //paymentId

� ����� ��������� &quot;webView&quot;, ����� �������� �������� ��������� �����:

        public void onCardPaid(Response response)

