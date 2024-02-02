package com.example.trasstarea.preferencias;

import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.example.trasstarea.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.preferencias));
        }

    }
    public void restablecerPreferencias(View view){
        SettingsFragment settingsFragment = (SettingsFragment) getSupportFragmentManager().findFragmentById(R.id.settings);

        if (settingsFragment != null) {
            settingsFragment.setDefaultValues();
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            // Escucha los cambios en la preferencia de tema
            findPreference("tema").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean tema = (boolean) newValue;

                    // Lógica para cambiar el tema según el valor seleccionado
                    if (tema) {

                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    } else {

                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }

                    return true;
                }
            });



            findPreference("letra").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String tamañoLetraString = (String) newValue;
                    // Convertir la cadena a entero con un valor predeterminado de 2 si falla
                    int tamañoLetraSeleccionado = Integer.parseInt(tamañoLetraString);
                    // Lógica para cambiar el tema según el valor seleccionado
                    switch (tamañoLetraSeleccionado) {
                        case 1:
                            setDefaultFontScale(0.7f);
                            break;
                        case 3:
                            setDefaultFontScale(1.4f);
                            break;
                        default:
                            setDefaultFontScale(1.0f);
                    }
                    restartSettingsFragment();
                    return true;
                }

            });

            SwitchPreference sdPreference = (SwitchPreference) findPreference("sd");
            sdPreference.setEnabled(false);//inhabilita el switch sd si no dispone de tarjeta sd el dispositivo
        }
        private void setDefaultValueSwitch(String key, boolean value) {
            SwitchPreference switchPreference = findPreference(key);
            if (switchPreference != null) {
                switchPreference.setChecked(value);
            }
        }

        private void setDefaultValueListOrEditText(String key, String value) {
            Preference preference = findPreference(key);

            if (preference instanceof EditTextPreference) {
                ((EditTextPreference) preference).setText(value);
            } else if (preference instanceof ListPreference) {
                ((ListPreference) preference).setValue(value);
            }
        }

        public void setDefaultValues() {
            setDefaultValueSwitch("tema", false);
            setDefaultValueListOrEditText("letra", "2");
            setDefaultValueListOrEditText("criterio", "2");
            setDefaultValueSwitch("orden", false);
            setDefaultValueSwitch("sd", false);
            setDefaultValueListOrEditText("limpieza", "0");
        }

        private boolean hasSDCard() {
            String state = Environment.getExternalStorageState();
            return Environment.MEDIA_MOUNTED.equals(state) ||
                    Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
        }
        private void restartSettingsFragment() {

            if (getActivity() != null) {
                Intent intent = getActivity().getIntent();
                getActivity().finish();
                getActivity().startActivity(intent);
            }
        }
        private void setDefaultFontScale(float factorDeEscala) {
            // Configura el factor de escala para toda la aplicación
            Resources resources = getResources();
            Configuration configuration = resources.getConfiguration();
            configuration.fontScale = factorDeEscala;
            resources.updateConfiguration(configuration, null);
        }

    }
    //Metodo para cerrar la actividad en el menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}