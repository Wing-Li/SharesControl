package com.lyl.sharescontrol.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lyl.sharescontrol.MyApp;
import com.lyl.sharescontrol.R;
import com.lyl.sharescontrol.net.NetWork;
import com.lyl.sharescontrol.net.entry.ShareEntry;

import java.text.DecimalFormat;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetPriceActivity extends AppCompatActivity {

    private Context mContext;

    @Bind(R.id.share_id)
    EditText shareId;
    @Bind(R.id.share_price)
    EditText sharePrice;
    @Bind(R.id.share_tall_ratio)
    EditText shareTallRatio;
    @Bind(R.id.share_low_ratio)
    EditText shareLowRatio;
    @Bind(R.id.share_tall)
    TextView shareTall;
    @Bind(R.id.share_low)
    TextView shareLow;
    @Bind(R.id.price_btn)
    Button priceBtn;

    private ShareEntry mShareEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_price);
        ButterKnife.bind(this);
        mContext = this;

        mShareEntry = new ShareEntry();
        setDate();
    }

    private void setDate() {
        shareId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                final String code = charSequence.toString().trim();
                if (code.length() < 6) {
                    return;
                }

                String shareCode = "";
                // 6开头为沪交所A股票、 9 上海B股
                // 0开头为深交所A股票、200 深证B股、300开头为创业板股票
                if (code.startsWith("6") || code.startsWith("9")) {
                    shareCode = "sh" + code;
                } else if (code.startsWith("0") || code.startsWith("200") || code.startsWith("300")) {
                    shareCode = "sz" + code;
                }

                // var hq_str_sz000507="珠海港,9.710,9.690,10.000,10.390,9.640,9.950,10.000,113588106,1139727356.610,8100,9.950,55300,9.940,
                // 73800,9.930,140900,9.920,152800,9.910,114450,10.000,63100,10.010,92844,10.020,68600,10.030,52200,10.040,2017-05-15,
                // 14:16:00,00";

                Call<String> stringCall = NetWork.getShareApi().getShare(shareCode);
                final String finalShareCode = shareCode;
                stringCall.clone().enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            String body = response.body();
                            int start = body.indexOf("=");
                            String content = body.substring(start + 2, body.length() - 3);
                            String[] codeArray = content.split(",");

                            mShareEntry.setCode(finalShareCode);
                            mShareEntry.setName(codeArray[0]);

                            shareId.setText(finalShareCode + "(" + codeArray[0] + ")");
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), R.string.net_code_error, Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // 修改价格
        sharePrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                compute(charSequence.toString(), shareTallRatio.getText().toString().trim(), shareLowRatio.getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // 修改止盈
        shareTallRatio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                compute(sharePrice.getText().toString().trim(), charSequence.toString(), shareLowRatio.getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // 修改止损
        shareLowRatio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                compute(sharePrice.getText().toString().trim(), shareTallRatio.getText().toString().trim(), charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        priceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shareIdStr = shareId.getText().toString().trim();
                String sharePriceStr = sharePrice.getText().toString().trim();
                String shareTallRatioStr = shareTallRatio.getText().toString().trim();
                String shareLowRatioStr = shareLowRatio.getText().toString().trim();
                String shareTallStr = shareTall.getText().toString().trim();
                String shareLowStr = shareLow.getText().toString().trim();

                if (em(shareIdStr) || em(sharePriceStr) || em(shareTallStr) || em(shareLowStr)) {
                    Toast.makeText(getApplicationContext(), "请输入相关数据", Toast.LENGTH_SHORT).show();
                    return;
                }

                mShareEntry.setBuyPrice(sharePriceStr);
                mShareEntry.setShareTallRatio(shareTallRatioStr);
                mShareEntry.setShareLowRatio(shareLowRatioStr);
                mShareEntry.setShareTall(shareTallStr);
                mShareEntry.setShareLow(shareLowStr);

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                String date = calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
                date = date + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar
                        .SECOND);
                mShareEntry.setCreateDate(date);
                mShareEntry.setDelete(false);

                MyApp.liteOrm.save(mShareEntry);
                Toast.makeText(getApplicationContext(), R.string.success, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    private boolean em(String s) {
        return TextUtils.isEmpty(s);
    }

    private void compute(String p, String t, String l) {
        float price = Float.parseFloat(p);
        float tallR = Float.parseFloat(t);
        float lowR = Float.parseFloat(l);

        DecimalFormat decimalFormat = new DecimalFormat("#.000");

        float tR = price + price * tallR / 100;
        shareTall.setText(String.valueOf(decimalFormat.format(tR)));
        float lR = price - price * lowR / 100;
        shareLow.setText(String.valueOf(decimalFormat.format(lR)));
    }

}
