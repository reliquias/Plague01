package br.com.inicial.util;

import java.util.Calendar;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * 
 * @author wReliquias
 */
public class TesteFirebase {

	public static void main(String[] args){
		dataProcessamento();
		System.out.println("1vez");
	}

	
	private static void dataProcessamento() {

		final String[] executou = { null };
		final String empresaCnpj = "cliente02";
		final Firebase firebase = new Firebase("https://baseagro-f1859.firebaseio.com/" + empresaCnpj+ "/bdos/");
		firebase.addValueEventListener(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot ds) {

				for (DataSnapshot boletimSnapshot : ds.getChildren()) {
					if (boletimSnapshot.child("dataProcessamento").getValue() != null) {
						Firebase firebase2 = new Firebase("https://baseagro-f1859.firebaseio.com/"+ empresaCnpj + "/bdos/"+ boletimSnapshot.getKey());
						firebase2.child("dataProcessamento").setValue(null);
					}
					for (DataSnapshot apontamentoSnapshot : boletimSnapshot.child("boletins").getChildren()) {
						if (apontamentoSnapshot.child("dataProcessamento").getValue() != null) {
							Firebase firebase3 = apontamentoSnapshot.getRef();
							firebase3.child("dataProcessamento").setValue(null);
						}
					}
				}
				System.out.println("Entrou");
				// }
				executou[0] = "true";
			}

			@Override
			public void onCancelled(FirebaseError fe) {

			}
		});

		while (executou[0] == null) {
			System.out.println("Dentro do while");
		}
	}
	
	private static void ouvirFirebase(){
		final Firebase firebase = new Firebase(
				"https://baseagro-f1859.firebaseio.com/cliente01/fazenda/");

		final String[] areaFazenda = { null };

		final String[] areaInicialFazenda = { null };

		firebase.addValueEventListener(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot ds) {

				for (DataSnapshot fazendaSnapshot : ds.getChildren()) {
					if (fazendaSnapshot.child("nome").getValue() != null) {
						String nomeFazenda = fazendaSnapshot.child("nome").getValue().toString();
						if (nomeFazenda.equals("DoRicardo")) {
							areaFazenda[0] = fazendaSnapshot.child("area").getValue().toString();
							areaInicialFazenda[0] = fazendaSnapshot.child("areaInicial").getValue().toString();
						}
					}
				}
//				System.out.println("Area Inicial = " + areaInicialFazenda[0]);
			}

			@Override
			public void onCancelled(FirebaseError fe) {
			}
		});
		
		while(areaInicialFazenda[0] == null){
		}
		System.out.println("Wanderson: "+areaInicialFazenda[0]);
	}
		
}
