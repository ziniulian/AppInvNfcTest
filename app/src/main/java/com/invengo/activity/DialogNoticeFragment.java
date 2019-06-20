package com.invengo.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

public class DialogNoticeFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_notice, null);
		
		AlertDialog dialog = new AlertDialog.Builder(getActivity())
			.setIcon(android.R.drawable.ic_dialog_info)
			.setView(dialogView)
			.setTitle(R.string.dialog_notice_title)
			.setPositiveButton(getString(R.string.dialog_notice_ok), new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent settingIntent = new Intent(Settings.ACTION_NFC_SETTINGS);
					startActivity(settingIntent);
				}
			})
			.setNegativeButton(getString(R.string.dialog_notice_cancel), new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			})
			.create();
		
		return dialog;
	}
	
}
