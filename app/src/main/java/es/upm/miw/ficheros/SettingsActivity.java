package es.upm.miw.ficheros;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.widget.Toast;
import java.io.File;

public class SettingsActivity extends Activity {
    public static SettingsActivity settingsActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
        settingsActivity = this;
    }

    public static class SettingsFragment extends PreferenceFragment implements
            Preference.OnPreferenceClickListener {
        private Preference eliminarPreference;
        private Preference mostrarPreference;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.ajustes);
            eliminarPreference = (Preference) findPreference("eliminar");
            eliminarPreference.setOnPreferenceClickListener(this);
            mostrarPreference = (Preference) findPreference("mostrar");
            mostrarPreference.setOnPreferenceClickListener(this);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            operatePreference(preference);
            return false;
        }

        private void operatePreference(Preference preference) {
            if (preference == eliminarPreference) {
                eliminarFicheros(preference.getContext());
                Log.i("elminiado", "Todos los datos de la applicacion han eliminado");
                Toast toast = Toast.makeText(getActivity(), "Todos los datos de la applicacion han eliminado", Toast.LENGTH_SHORT);
                toast.show();
            } else if (preference == mostrarPreference) {
                FilesDialog(preference.getContext());
                Log.i("mostrado", "Los ficheros generados han mostrado");
            }
        }
    }

    public static void eliminarFicheros(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
        deleteFilesByDirectory(context.getExternalFilesDir(null));

    }


    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    public static void FilesDialog(Context context) {
        File rutaSD = context.getExternalFilesDir(null);
        int i;
        String namesSD = "";
        assert rutaSD != null;
        File[] filesSD = rutaSD.listFiles();
        Log.i("lengthSD", "length of arraySD:" + filesSD.length);
        for (i = 0; i < filesSD.length; i++) {
            namesSD += filesSD[i].getName() + '\n';
        }

        File rutaLocal = context.getFilesDir();
        int j;
        String namesLocal = "";
        assert rutaLocal != null;
        File[] filesLocal = rutaLocal.listFiles();
        Log.i("lengthLocal", "length of arrayLocal:" + filesLocal.length);
        for (j = 0; j < filesLocal.length; j++) {
            namesLocal += filesLocal[j].getName() + '\n';
        }

        new AlertDialog.Builder(SettingsActivity.settingsActivity)
                .setTitle("Los ficheros generados")
                .setMessage("Los ficheros de la tarjeta SD:"+'\n'+namesSD
                +'\n'+"Todos los ficheros locales:"+'\n'+namesLocal)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

}

