package com.sydehealth.sydehealth.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sydehealth.sydehealth.utility.Definitions;
import com.sydehealth.sydehealth.utility.SaveData;
import com.sydehealth.sydehealth.utility.UsefullData;
import com.sydehealth.sydehealth.R;

import androidx.appcompat.app.AppCompatActivity;

public class Screenshare_instruction_page extends AppCompatActivity {

    TextView instruction;
    UsefullData usefullData;
    SaveData saveData;
    Button skip_btn, done_btn;
    TextView step1, step2, computer_txt, search_txt, chrome_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screenshare_instruction_page);

        usefullData=new UsefullData(Screenshare_instruction_page.this);
        saveData=new SaveData(Screenshare_instruction_page.this);
        instruction=(TextView) findViewById(R.id.instruction);
        instruction.setTypeface(usefullData.get_montserrat_regular());
        skip_btn=(Button) findViewById(R.id.skip_btn);
        done_btn=(Button) findViewById(R.id.done_btn);
        skip_btn.setTypeface(usefullData.get_montserrat_regular());
        done_btn.setTypeface(usefullData.get_montserrat_regular());

        step1=(TextView) findViewById(R.id.step1);
        step2=(TextView) findViewById(R.id.step2);
        computer_txt=(TextView) findViewById(R.id.computer_txt);
        search_txt=(TextView) findViewById(R.id.search_txt);
        chrome_txt=(TextView) findViewById(R.id.chrome_txt);

        step1.setTypeface(usefullData.get_montserrat_bold());
        step2.setTypeface(usefullData.get_montserrat_bold());
        computer_txt.setTypeface(usefullData.get_montserrat_regular());
        search_txt.setTypeface(usefullData.get_montserrat_regular());
        chrome_txt.setTypeface(usefullData.get_montserrat_regular());

        search_txt.setText("Navigate to\n"+ Definitions.APIdomain.replace("https://",""));

        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData.save(Definitions.SCREENSHARE_NOTIFICATION,true);
                Intent forgot=new Intent(Screenshare_instruction_page.this,Tab_activity.class);
                startActivity(forgot);
                finish();
            }
        });
        skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData.save(Definitions.SCREENSHARE_NOTIFICATION,false);
                Intent forgot=new Intent(Screenshare_instruction_page.this,Tab_activity.class);
                startActivity(forgot);
                finish();
            }
        });

    }
}
