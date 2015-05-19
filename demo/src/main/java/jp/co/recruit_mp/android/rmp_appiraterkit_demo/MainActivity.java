/*
 * Copyright (C) 2015 Recruit Marketing Partners Co.,Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.co.recruit_mp.android.rmp_appiraterkit_demo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import jp.co.recruit_mp.android.rmp_appiraterkit.AppiraterDialogBuilder;
import jp.co.recruit_mp.android.rmp_appiraterkit.AppiraterMeasure;
import jp.co.recruit_mp.android.rmp_appiraterkit.AppiraterUtils;

public class MainActivity extends ActionBarActivity {

    private static final String PREF_KEY_APP_ALREADY_RATE = "PREF_KEY_APP_ALREADY_RATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Notify app launch to RmpAppiraterKit.
        AppiraterMeasure.appLaunched(this);

        Button button = (Button) findViewById(R.id.tap_me);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showDialog() {
        final SharedPreferences prefs = getSharedPreferences("Settings", Context.MODE_PRIVATE);

        // Gets last AppiraterMeasure#appLaunched(android.content.Context) result.
        AppiraterMeasure.Result appiraterResult = AppiraterMeasure.getLastAppLaunchedResult();
        if (
            // Not select "Yes, Rate" button yet. or ...
            !prefs.getBoolean(PREF_KEY_APP_ALREADY_RATE, false) ||
            // Change application version.
            (appiraterResult != null &&
                    appiraterResult.getAppVersionCode() != appiraterResult.getPreviousAppVersionCode())) {
            final int applicationNameResId = this.getApplicationInfo().labelRes;
            final String applicationName = this.getString(applicationNameResId);

            // Create app review dialog.
            AppiraterDialogBuilder builder = new AppiraterDialogBuilder(this);
            builder
                    .setTitle(getString(R.string.title, applicationName))
                    .setMessage(R.string.message)
                    .addButton(R.string.rate_star5, new AppiraterDialogBuilder.OnClickListener() {
                        @Override
                        public void onClick(Dialog dialog) {
                            SharedPreferences.Editor prefsEditor = prefs.edit();
                            prefsEditor.putBoolean(PREF_KEY_APP_ALREADY_RATE, true);
                            prefsEditor.apply();

                            AppiraterUtils.launchStore(MainActivity.this);
                            dialog.dismiss();
                        }
                    })
                    .addButton(R.string.report_problem, new AppiraterDialogBuilder.OnClickListener() {
                        @Override
                        public void onClick(Dialog dialog) {
                            Uri uri = Uri.parse("http://www.google.com/"); // Inputs your supports site.
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    })
                    .addButton("You have already rated this app", new AppiraterDialogBuilder.OnClickListener() {
                        @Override
                        public void onClick(Dialog dialog) {
                            dialog.dismiss();
                        }
                    });
            Dialog dialog = builder.create();
            dialog.show();
        } else {
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage(R.string.already_rated)
                    .setPositiveButton(R.string.close, null)
                    .show();
        }
    }
}
