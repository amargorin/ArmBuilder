package pro.matyschenko.armbuilder.armbuilder.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import pro.matyschenko.armbuilder.armbuilder.Meter;
import pro.matyschenko.armbuilder.armbuilder.R;
import pro.matyschenko.armbuilder.armbuilder.adapter.MeterListAdapter;
import pro.matyschenko.armbuilder.armbuilder.dto.MeterDTO;


public class MeasurementFragment extends AbstractTabFragment {
    Meter meter;
    long zero_value = 8416300; // Калибровочный коэффициент
    long input_value = 0;
    MeterListAdapter meterListAdapter;

    public static MeasurementFragment newInstance(Context context) {
        MeasurementFragment fragment = new MeasurementFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.measurement_fragment));
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_measurement, container, false);
        meter = (Meter) v.findViewById(R.id.meter);
        meter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                zero_value = input_value;
                meter.moveToValue(0);

                }
        }
        );
        RecyclerView rv = v.findViewById(R.id.recycle_measure);
        rv.setLayoutManager(new LinearLayoutManager(context));
        meterListAdapter = new MeterListAdapter(createMockMeterListData());
        rv.setAdapter(meterListAdapter);

        return v;
    }

    private List<MeterDTO> createMockMeterListData() {
        List<MeterDTO> data = new ArrayList<>();
        data.add(new MeterDTO("11.00"));
        data.add(new MeterDTO("12.00"));
        data.add(new MeterDTO("13.00"));
        data.add(new MeterDTO("14.00"));
        data.add(new MeterDTO("15.00"));
        data.add(new MeterDTO("15.00"));
        data.add(new MeterDTO("15.00"));
        return data;
    }

}
