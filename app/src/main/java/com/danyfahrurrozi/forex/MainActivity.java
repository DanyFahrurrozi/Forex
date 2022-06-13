package com.danyfahrurrozi.forex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.text.DecimalFormat;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private ProgressBar loadingProgressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView chfTextView, sarTextView, cupTextView, eurTextView, idrTextView, ilsTextView, rubTextView, usdTextView, lydTextView, iqdTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout1);
        chfTextView = (TextView)findViewById(R.id.chfTextView);
        sarTextView = (TextView)findViewById(R.id.sarTextView);
        cupTextView = (TextView)findViewById(R.id.cupTextView);
        eurTextView = (TextView)findViewById(R.id.eurTextView);
        idrTextView = (TextView)findViewById(R.id.idrTextView);
        ilsTextView = (TextView)findViewById(R.id.ilsTextView);
        rubTextView = (TextView)findViewById(R.id.rubTextView);
        usdTextView = (TextView)findViewById(R.id.usdTextView);
        lydTextView = (TextView)findViewById(R.id.lydTextView);
        iqdTextView = (TextView)findViewById(R.id.iqdTextView);
        loadingProgressBar = (ProgressBar)findViewById(R.id.loadingProgressBar);

        initSwipeRefreshLayout();
        initForex();
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            initForex();

            swipeRefreshLayout.setRefreshing(false);
        });
    }

    public String formatNumber(double number, String format) {
        DecimalFormat decimalFormat = new DecimalFormat(format);
        return decimalFormat.format(number);
    }

    private void initForex() {
        loadingProgressBar.setVisibility(TextView.VISIBLE);

        String url = "https://openexchangerates.org/api/latest.json?app_id=0e40052bcbe642769eb2c414a450ac29";

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            Gson gson = new Gson();
            RootModel rootModel = gson.fromJson(new String(responseBody), RootModel.class);
            RatesModel ratesModel = rootModel.getRatesModel();

            double chf = ratesModel.getIDR() / ratesModel.getCHF();
            double sar = ratesModel.getIDR() / ratesModel.getSAR();
            double cup = ratesModel.getIDR() / ratesModel.getCUP();
            double eur = ratesModel.getIDR() / ratesModel.getEUR();
            double ils = ratesModel.getIDR() / ratesModel.getILS();
            double rub = ratesModel.getIDR() / ratesModel.getRUB();
            double usd = ratesModel.getIDR() / ratesModel.getUSD();
            double lyd = ratesModel.getIDR() / ratesModel.getLYD();
            double iqd = ratesModel.getIDR() / ratesModel.getIQD();
            double idr = ratesModel.getIDR();

            chfTextView.setText(formatNumber(chf, "###,##0.##"));
            sarTextView.setText(formatNumber(sar, "###,##0.##"));
            cupTextView.setText(formatNumber(cup, "###,##0.##"));
            eurTextView.setText(formatNumber(eur, "###,##0.##"));
            idrTextView.setText(formatNumber(idr, "###,##0.##"));
            ilsTextView.setText(formatNumber(ils, "###,##0.##"));
            rubTextView.setText(formatNumber(rub, "###,##0.##"));
            usdTextView.setText(formatNumber(usd, "###,##0.##"));
            lydTextView.setText(formatNumber(lyd, "###,##0.##"));
            iqdTextView.setText(formatNumber(iqd, "###,##0.##"));

            loadingProgressBar.setVisibility(TextView.INVISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                loadingProgressBar.setVisibility(TextView.INVISIBLE);
            }
        });
    }


}