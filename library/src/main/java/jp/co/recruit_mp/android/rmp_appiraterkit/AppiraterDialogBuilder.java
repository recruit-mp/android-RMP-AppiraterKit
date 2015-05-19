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

package jp.co.recruit_mp.android.rmp_appiraterkit;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Dialog builder.
 */
public class AppiraterDialogBuilder {

    private Context mContext;

    private CharSequence mTitle;

    private CharSequence mMessage;

    private ArrayList<ButtonData> mButtonDataList = new ArrayList<>(2);

    public AppiraterDialogBuilder(Context context) {
        mContext = context;
    }

    /**
     * Sets dialog title.
     *
     * @param title Dialog title
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public AppiraterDialogBuilder setTitle(CharSequence title) {
        mTitle = title;
        return this;
    }

    /**
     * Sets dialog title.
     *
     * @param resId Dialog title
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public AppiraterDialogBuilder setTitle(int resId) {
        mTitle = mContext.getString(resId);
        return this;
    }

    /**
     * Sets dialog message.
     *
     * @param message Dialog message
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public AppiraterDialogBuilder setMessage(CharSequence message) {
        mMessage = message;
        return this;
    }

    /**
     * Sets dialog message.
     *
     * @param resId Dialog message
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public AppiraterDialogBuilder setMessage(int resId) {
        mMessage = mContext.getString(resId);
        return this;
    }

    /**
     * Adds button to dialog.
     *
     * @param text     Button text
     * @param listener Button click listener
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public AppiraterDialogBuilder addButton(CharSequence text, OnClickListener listener) {
        mButtonDataList.add(new ButtonData(text, listener));
        return this;
    }

    /**
     * Adds button to dialog.
     *
     * @param resId    Button text
     * @param listener Button click listener
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public AppiraterDialogBuilder addButton(int resId, OnClickListener listener) {
        return addButton(mContext.getString(resId), listener);
    }

    /**
     * Clear button from dialog.
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public AppiraterDialogBuilder clearButton() {
        mButtonDataList.clear();
        return this;
    }

    /**
     * Create dialog.
     *
     * @return Dialog
     */
    public Dialog create() {
        final Dialog dialog = new Dialog(mContext);
        dialog.setCancelable(true);

        LinearLayout layout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.rmp_appiraterkit_dialog, null);
        // Sets title
        TextView titleView = (TextView) layout.findViewById(R.id.title);
        if (titleView != null) {
            titleView.setText(mTitle);
        }
        // Sets message
        TextView messageView = (TextView) layout.findViewById(R.id.message);
        if (messageView != null) {
            messageView.setText(mMessage);
        }

        // Sets buttons
        for (final ButtonData buttonData : mButtonDataList) {
            View buttonLayout = LayoutInflater.from(mContext).inflate(R.layout.rmp_appiraterkit_dialog_button, null);
            Button button = (Button)buttonLayout.findViewById(R.id.button);
            if (buttonData != null) {
                button.setText(buttonData.getText());
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OnClickListener listener = buttonData.getListener();
                        if (listener != null) {
                            listener.onClick(dialog);
                        }
                    }
                });
            }
            layout.addView(buttonLayout);
        }

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);

        return dialog;
    }

    /**
     * Button data
     */
    private static class ButtonData {

        private CharSequence mText;

        private OnClickListener mListener;

        /**
         * Constractor
         *
         * @param text     Button text
         * @param listener Button click listener
         */
        private ButtonData(CharSequence text, OnClickListener listener) {
            mText = text;
            mListener = listener;
        }

        /**
         * Gets button text.
         *
         * @return Button text
         */
        public CharSequence getText() {
            return mText;
        }

        /**
         * Gets button click listener.
         *
         * @return Button click listener
         */
        public OnClickListener getListener() {
            return mListener;
        }
    }

    /**
     * Button click listener interface.
     */
    public static interface OnClickListener {
        void onClick(Dialog dialog);
    }
}