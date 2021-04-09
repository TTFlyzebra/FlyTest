package com.octopus.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.location.Location;
import android.octopu.OctopuManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CommandActivity extends AppCompatActivity {
    private static final String TAG = "Command";
    private EditText command;
    private EditText param1;
    private EditText param2;
    private TextView result;
    private OctopuManager octopuManager;
    private ArrayList<Command> commands = new ArrayList<Command>();
    private class Command {
        private String command;
        private String param1;
        private String param2;
        Command(String command) {
            this.command = command;
            this.param1 = "";
            this.param2 = "";
        }
        Command(String command, String param1, String param2) {
            this.command = command;
            this.param1 = param1 == null ? "" : param1;
            this.param2 = param2 == null ? "" : param2;
        }
        public String  getCommand() { return this.command; }
        public String  getParam1() { return this.param1; }
        public String  getParam2() { return this.param2; }
        public String start(String param1, String param2) {
            return "Error, don't use this start";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command);
        command = findViewById(R.id.editTextCommand);
        param1 = findViewById(R.id.editTextParam1);
        param2 = findViewById(R.id.editTextParam2);
        result = findViewById(R.id.textViewResult);
        octopuManager = (OctopuManager) getSystemService("octopu");
        addAllCommand();
    }

    private void addAllCommand() {
        commands.add(new Command("test") {
            @Override
            public String start(String param1, String param2) {
                return test(param1, param2);
            }
        });

        commands.add(new Command("airplane",
                "[true | false]",
                "") {
            @Override
            public String start(String param1, String param2) {
                return airplane(param1, param2);
            }
        });

        commands.add(new Command("phonebook",
                "[ save | delete-one | delete-all ]",
                "name,number,isSim[true | false]") {
            @Override
            public String start(String param1, String param2) {
                return phonebook(param1, param2);
            }
        });

        commands.add(new Command("getcellinfo") {
            @Override
            public String start(String param1, String param2) {
                return getcellinfo(param1, param2);
            }
        });

        commands.add(new Command("setlocation",
                "longitude,latitude",
                "accuracy,altitude,bearing,speed") {
            @Override
            public String start(String param1, String param2) {
                return setlocation(param1, param2);
            }
        });

        commands.add(new Command("getcelllocation") {
            @Override
            public String start(String param1, String param2) {
                return getcelllocation(param1, param2);
            }
        });
    }

    public void support(View view) {
        //clear(view);
        StringBuilder sb = new StringBuilder();
        for (Command c : commands) {
            sb.append("command: ").append(c.getCommand()).append("\n")
               .append("param1: ").append(c.getParam1()).append("\n")
               .append("param2: ").append(c.getParam2()).append("\n\n");
        }
        result.setText(sb.toString());
        //关闭键盘
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clear(View view) {
        command.setText("");
        param1.setText("");
        param2.setText("");
        result.setText("");
    }

    public void start(View view) {
        String resultStr;
        String commandStr = command.getText().toString();
        if (TextUtils.isEmpty(commandStr)) {
            resultStr = "Command Empty";
        } else {
            resultStr = doCommand(commandStr.trim(),
                                  param1.getText().toString().trim(),
                                  param2.getText().toString().trim());
        }
        result.setText(resultStr);
    }

    private String doCommand(String commandStr, String param1, String param2) {
        Log.d(TAG, "doCommand: "+ commandStr + ", " + param1 + ", " + param2);
        for (Command c : commands) {
            if (c.getCommand().equals(commandStr)) {
                return c.start(param1, param2);
            }
        }
        return "Unknown Command!";
    }

    private String test(String param1, String param2) {
        return "It's test command!";
    }

    private String airplane(String param1, String param2) {
        boolean on;
        if ("".equals(param1)) return "Usage:\nparam1: [true | false]";
        if ("true".equals(param1))
            on = true;
        else if ("false".equals(param1))
            on = false;
        else
            return "Usage:\nparam1: [true | false]";

        octopuManager.setAirplaneModeOn(on);
        return "set airplane sucessfully.";
    }

    private String phonebook(String param1, String param2) {
        int action;
        String name = "";
        String number = "";
        boolean isSim = false;
        if ("save".equals(param1)) {
            action = OctopuManager.PB_ACTION_SAVE;
        } else if ("delete-one".equals(param1)) {
            action = OctopuManager.PB_ACTION_DELETE_ONE;
        } else if ("delete-all".equals(param1)) {
            action = OctopuManager.PB_ACTION_DELETE_ALL;
        } else {
            return "Usage:\nparam1: [save | delete-one | delete-all]\nparam2: name,number,isSim";
        }
        if (action != OctopuManager.PB_ACTION_DELETE_ALL) {
            if ("".equals(param2)) return "param2 don't empty";
            String[] contact = param2.split(",");
            if (contact.length < 2) return "param2 usage: name,number,isSim";

            name = contact[0].trim();
            number = contact[1].trim();
            if (contact.length == 3) {
                String sim = contact[2].trim();
                if ("true".equals(sim))
                    isSim = true;
                else if ("false".equals(sim))
                    isSim = false;
                else
                    return "isSim error:  [true | false]";
            }
        }

        Bundle bundle = new Bundle();
        bundle.putInt(OctopuManager.PB_ACTION, action);
        bundle.putBoolean(OctopuManager.PB_SIMCARD, isSim);
        bundle.putString(OctopuManager.PB_NAME, name);
        bundle.putString(OctopuManager.PB_NUMBER, number);
        octopuManager.upPhonebookData(bundle);
        return param1 + " sucessfully, please check Dialer app";
    }

    private String getcellinfo(String param1, String param2) {
        StringBuilder ret = new StringBuilder();
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        List<CellInfo> cellinfo = tm.getAllCellInfo();
        int size = cellinfo.size();
        ret.append("cell list count = ").append(size).append("\n");
        for (int i = 0; i < size; i++) {
            ret.append(i+1).append(":").append("\n")
               .append(cellinfo.get(i))
               .append("\n\n");
        }
        return ret.toString();
    }

    private String setlocation(String param1, String param2) {
        if (TextUtils.isEmpty(param1)) return "Usage:\nparam1: longitude,latitude\nparam2: accuracy,altitude,bearing,speed";
        String[] gps = param1.split(",");
        if (gps.length != 2) return "param1: longitude,latitude";

        String longitude = gps[0].trim();
        String latitude = gps[1].trim();
        String accuracy = null;
        String altitude = null;
        String bearing = null;
        String speed = null;
        if (!TextUtils.isEmpty(param2)) {
            String[] other = param2.split(",");
            if (other.length > 0) accuracy = other[0].trim();
            if (other.length > 1) altitude = other[1].trim();
            if (other.length > 2) bearing = other[2].trim();
            if (other.length > 3) speed = other[3].trim();
        }
        Bundle bundle = new Bundle();
        Location loc = new Location("gps");
        try {
            loc.setLongitude(Double.valueOf(longitude));
            loc.setLatitude(Double.valueOf(latitude));

            if (!TextUtils.isEmpty(accuracy))
                loc.setAccuracy(Float.valueOf(accuracy));
            if (!TextUtils.isEmpty(altitude))
                loc.setAltitude(Double.valueOf(altitude));
            if (!TextUtils.isEmpty(bearing))
                loc.setBearing(Float.valueOf(bearing));
            if (!TextUtils.isEmpty(speed))
                loc.setSpeed(Float.valueOf(speed));
        } catch (NumberFormatException e) {
            return "param error: " + e.toString();
        }
        loc.setTime(SystemClock.elapsedRealtime());
        loc.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        bundle.putParcelable(OctopuManager.GPS_LOCATION,loc);
        octopuManager.upGpsData(bundle);
        return "successfully!\n\n" + loc.toString();
    }

    private String getcelllocation(String param1, String param2) {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        CellLocation celllocation = tm.getCellLocation();
        StringBuilder ret = new StringBuilder();
        if (celllocation instanceof GsmCellLocation) {
            ret.append("mLac=").append(((GsmCellLocation) celllocation).getLac())
                    .append(", mCid=").append(((GsmCellLocation) celllocation).getCid())
                    .append(", mPsc=").append(((GsmCellLocation) celllocation).getPsc());
        } else {
            ret.append("Sorry, Don't support CDMA");
        }
        return ret.toString();
    }
}