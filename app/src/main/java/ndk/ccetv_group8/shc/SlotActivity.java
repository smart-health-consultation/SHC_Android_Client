package ndk.ccetv_group8.shc;

import android.app.SearchManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Time;
import java.util.ArrayList;

import ndk.ccetv_group8.shc.models.ConsultationSlot;

public class SlotActivity extends AppCompatActivity {

    String doctor = "XYZ";
    String passedDoctor;
    TextView textViewSelectedConsultationSlot;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private ConsultationSlotRecyclerViewAdapter mAdapter;
    private ArrayList<ConsultationSlot> modelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation_slot);

        textViewSelectedConsultationSlot = findViewById(R.id.textViewSelectedConsultationSlot);

        passedDoctor = getIntent().getStringExtra("Doctor");
        if (passedDoctor == null) {
            passedDoctor = doctor;
        }

        TextView textViewDisease = findViewById(R.id.textViewDisease);
        textViewDisease.setText(getResources().getString(R.string.doctorWithFullColumn) + passedDoctor);

        findViews();
        setSupportActionBar(toolbar);
        setAdapter();

        Button buttonSubmit = findViewById(R.id.buttonSubmit);
        Button buttonDoctors = findViewById(R.id.buttonDoctors);

        buttonDoctors.setOnClickListener(ButtonUtils.getBackButtonEvent(this));
        buttonSubmit.setOnClickListener(ButtonUtils.getStartActivityWithFinishButtonEvent(this, SlotConfirmationActivity.class, ApplicationConstants.APPLICATION_NAME));
    }

    private void findViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_search, menu);

        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat
                .getActionView(menu.findItem(R.id.action_search));

        SearchManager searchManager = (SearchManager) this.getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));

        //changing editText color
        EditText searchEdit = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEdit.setTextColor(Color.WHITE);
        searchEdit.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        searchEdit.setHintTextColor(Color.WHITE);
        searchEdit.setBackgroundColor(Color.TRANSPARENT);
        searchEdit.setHint("Search");

        InputFilter[] fArray = new InputFilter[2];
        fArray[0] = new InputFilter.LengthFilter(40);
        fArray[1] = (source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++) {
                if (!Character.isLetterOrDigit(source.charAt(i)))
                    return "";
            }
            return null;
        };
        searchEdit.setFilters(fArray);
        View v = searchView.findViewById(androidx.appcompat.R.id.search_plate);
        v.setBackgroundColor(Color.TRANSPARENT);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<ConsultationSlot> filterList = new ArrayList<ConsultationSlot>();
                if (s.length() > 0) {
                    for (int i = 0; i < modelList.size(); i++) {
                        if (String.valueOf(modelList.get(i).getSlotStart()).toLowerCase().contains(s.toLowerCase()) || String.valueOf(modelList.get(i).getSlotEnd()).toLowerCase().contains(s.toLowerCase())) {
                            filterList.add(modelList.get(i));
                            mAdapter.updateList(filterList);
                        }
                    }
                } else {
                    mAdapter.updateList(modelList);
                }
                return false;
            }
        });
        return true;
    }

    private void setAdapter() {

//        modelList.add(new AbstractModel("Android", "Hello " + " Android"));
//        modelList.add(new AbstractModel("Beta", "Hello " + " Beta"));
//        modelList.add(new AbstractModel("Cupcake", "Hello " + " Cupcake"));
//        modelList.add(new AbstractModel("Donut", "Hello " + " Donut"));
//        modelList.add(new AbstractModel("Eclair", "Hello " + " Eclair"));
//        modelList.add(new AbstractModel("Froyo", "Hello " + " Froyo"));
//        modelList.add(new AbstractModel("Gingerbread", "Hello " + " Gingerbread"));
//        modelList.add(new AbstractModel("Honeycomb", "Hello " + " Honeycomb"));
//        modelList.add(new AbstractModel("Ice Cream Sandwich", "Hello " + " Ice Cream Sandwich"));
//        modelList.add(new AbstractModel("Jelly Bean", "Hello " + " Jelly Bean"));
//        modelList.add(new AbstractModel("KitKat", "Hello " + " KitKat"));
//        modelList.add(new AbstractModel("Lollipop", "Hello " + " Lollipop"));
//        modelList.add(new AbstractModel("Marshmallow", "Hello " + " Marshmallow"));
//        modelList.add(new AbstractModel("Nougat", "Hello " + " Nougat"));
//        modelList.add(new AbstractModel("Android O", "Hello " + " Android O"));

//        modelList.add(new Doctor(1, "Doctor", "Address", "Designation", "Working Hospital", "Certificate ID", "Working Clinic", new Time(0), new Time(0), 500.0));

        modelList.add(new ConsultationSlot(new Time(0), new Time(0)));
        mAdapter = new ConsultationSlotRecyclerViewAdapter(SlotActivity.this, modelList, "Time Slots");

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener((view, position, model) -> {
            //handle item click events here
            Toast.makeText(SlotActivity.this, "Hey " + model.getSlotStart(), Toast.LENGTH_SHORT).show();
            textViewSelectedConsultationSlot.setText("You are selected " + model.getSlotStart() + " to " + model.getSlotEnd());
        });

        mAdapter.SetOnHeaderClickListener((view, headerTitle) -> {
            //handle item click events here
        });
    }
}