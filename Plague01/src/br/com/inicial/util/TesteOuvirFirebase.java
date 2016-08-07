package br.com.inicial.util;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * 
 * @author roger
 */
public class TesteOuvirFirebase {

	public static void main(String[] args){
		ouvirFirebase();
		System.out.println("1vez");
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
