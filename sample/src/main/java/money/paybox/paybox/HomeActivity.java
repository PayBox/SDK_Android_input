package money.paybox.paybox;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import money.paybox.paybox.Fragments.FragmentCard;
import money.paybox.paybox.Fragments.FragmentPaymentA;
import money.paybox.paybox.Fragments.FragmentPaymentB;
import money.paybox.payboxsdk.Interfaces.PBListener;
import money.paybox.payboxsdk.Model.Capture;
import money.paybox.payboxsdk.Model.Card;
import money.paybox.payboxsdk.Model.Error;
import money.paybox.payboxsdk.Model.PStatus;
import money.paybox.payboxsdk.Model.RecurringPaid;
import money.paybox.payboxsdk.Model.Response;
import money.paybox.payboxsdk.PBHelper;

public class HomeActivity extends AppCompatActivity implements PBListener {

    ViewPager mainView;
    ViewPagerAdapter viewPagerAdapter;
    TabLayout tabLayout;
    private FragmentCard fragmentCard;
    private FragmentPaymentA fragmentPaymentA;
    private FragmentPaymentB fragmentPaymentB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mainView = (ViewPager)findViewById(R.id.mainView);
        tabLayout = (TabLayout)findViewById(R.id.tabs);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragmentPaymentA = new FragmentPaymentA();
        fragmentPaymentB = new FragmentPaymentB();
        fragmentCard = new FragmentCard();
        fragments.add(fragmentPaymentA);
        fragments.add(fragmentPaymentB);
        fragments.add(fragmentCard);
        viewPagerAdapter = new ViewPagerAdapter(getApplicationContext(), getSupportFragmentManager(), fragments);
        mainView.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(mainView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PBHelper.getSdk().removePbListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.instance.initBuilder(App.instance.getSecretKey(), App.instance.getMerchandID(), App.instance.getAutoClearing());
        App.instance.builder.build();
        PBHelper.getSdk().registerPbListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem mi = menu.getItem(0);
        mi.setIntent(new Intent(this, SettingActivity.class));
        return true;
    }

    @Override
    public void onCardList(ArrayList<Card> cards) {
        if(cards.isEmpty()){
            fragmentCard.showCardResult.setText("");
        }
        String message = new String();
        for(Card card : cards){
            message += "Card hash = "+card.getCardhash()+"\n"+
                    "Card ID = "+card.getCardId()+"\n"+
                    "Recurring profile = "+card.getRecurringProfile()+"\n"+
                    "Created At = "+card.getDate()+"\n"+
                    "Status = "+card.getStatus()+"\n\n";

        }
        fragmentCard.showCardResult.setText(message);
    }

    @Override
    public void onPaymentRevoke(Response response) {
        fragmentPaymentA.payRevokeResult.setText("Status = "+response.getStatus());
    }

    @Override
    public void onPaymentPaid(Response response) {
        fragmentPaymentA.payInitResult.setText("Payment ID = "+response.getPaymentId()+"\nStatus = "+response.getStatus());
    }

    @Override
    public void onPaymentStatus(PStatus pStatus) {
        fragmentPaymentB.statusPayResult.setText("Status = "+pStatus.getStatus()+"\nPayment system = "+pStatus.getPaymentSystem()+"\nTransaction Status = "+pStatus.getTransactionStatus()+"\nCaptured = "+pStatus.isCaptured()+"\nCan reject = "+pStatus.isCanReject()+"\nCard pan = "+pStatus.getCardPan());
    }

    @Override
    public void onCardAdded(Response response) {
        fragmentCard.cardAddResult.setText("Payment ID = "+response.getPaymentId()+
                "\nStatus = "+response.getStatus());
    }

    @Override
    public void onCardRemoved(Card card) {
        if(card!=null) {
            fragmentCard.remCardResult.setText("\nDeleted At = " + card.getDate() +
                    "\nStatus = " + card.getStatus());
        } else {
            fragmentCard.remCardResult.setText("");
        }
    }

    @Override
    public void onCardPayInited(Response response) {
        fragmentPaymentA.payRevokeResult.setText("Status = "+response.getStatus()+"\n"+
                "Payment ID = "+response.getPaymentId());
        PBHelper.getSdk().payWithCard(Integer.parseInt(response.getPaymentId()));
    }

    @Override
    public void onCardPaid(Response response) {
        fragmentCard.cardPayResult.setText("Payment ID = "+response.getPaymentId()+"\nStatus = "+response.getStatus());
    }

    @Override
    public void onRecurringPaid(RecurringPaid recurringPaid) {
        fragmentPaymentB.recurPayResult.setText("Payment ID = "+recurringPaid.getPaymentId()+"\nStatus = "+recurringPaid.getStatus()+"\nCurrency = "+recurringPaid.getCurrency()+"\nDate = "+recurringPaid.getExpireDate().toGMTString());
    }

    @Override
    public void onPaymentCaptured(Capture capture) {
        fragmentPaymentB.captureResult.setText("Status = "+capture.getStatus()+"\nAmount = "+capture.getAmount()+"\nClearing Amount = "+capture.getClearingAmount());
    }

    @Override
    public void onPaymentCanceled(Response response) {
        fragmentPaymentA.payCancelResult.setText("Status = "+response.getStatus());
    }

    @Override
    public void onError(Error error) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),error.getErrorDesription(),Snackbar.LENGTH_INDEFINITE);
        snackbar.setDuration(5000);
        snackbar.show();
    }
}
