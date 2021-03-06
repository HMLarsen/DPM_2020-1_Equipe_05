package br.com.furb.trabalhofinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.concurrent.ExecutionException;

import br.com.furb.trabalhofinal.cep.CEP;
import br.com.furb.trabalhofinal.cep.HttpService;
import br.com.furb.trabalhofinal.model.EventPojo;
import br.com.furb.trabalhofinal.service.EventService;

public class AddEvent extends AppCompatActivity {

    private Button addEvent;
    private TextView eventName;
    private TextView eventDesc;
    private TextView eventDate;
    private TextView eventHour;
    private TextView eventAdre;
    private TextView eventCEP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        addEvent = findViewById(R.id.btAddEvent);
        eventName = findViewById(R.id.ptEventName);
        eventDesc = findViewById(R.id.ptEventDesc);
        eventDate = findViewById(R.id.ptEventDate);
        eventHour = findViewById(R.id.ptEventHour);
        eventAdre = findViewById(R.id.ptEventAdre);
        eventCEP = findViewById(R.id.ptEventCEP);

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvent();
            }
        });

        eventCEP.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    try {
                        CEP retorno = new HttpService(eventCEP.getText().toString()).execute().get();
                        String endereco = retorno.getCidade() + " " + retorno.getEstado() + ", " + retorno.getLogradouro() + " " + retorno.getComplemento();
                        eventAdre.setText(endereco);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        // Botão para retornar para lista de eventos
        Button BtBackToList = (Button) findViewById(R.id.btBackToList);
        BtBackToList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(AddEvent.this, Lista.class);
                startActivity(it);
            }
        });
    }



    public void addEvent() {
        EventPojo event = new EventPojo();
        event.setNome(eventName.getText().toString());
        event.setDescricao(eventDesc.getText().toString());
        event.setData(eventDate.getText().toString());
        event.setHorario(eventHour.getText().toString());
        event.setEndereco(eventAdre.getText().toString());
        event.setCep(eventCEP.getText().toString());

        EventService eventService = new EventService();
        eventService.put(event)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

}