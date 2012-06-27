package agrobot.navigo;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;

public class Arquivos {

	public void gravaArquivo(String nome, String texto) {
		try {
			String lstrNomeArq = nome;

			File arq = new File(Environment.getExternalStorageDirectory(),
					lstrNomeArq);
			FileOutputStream fos;

			// transforma o texto digitado em array de bytes
			byte[] dados;

			Date date = new Date();
			DateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
			String formattedDate = formato.format(date);
			texto = formattedDate + texto + "\n";
			dados = texto.getBytes();
			fos = new FileOutputStream(arq, true);

			// escreve os dados e fecha o arquivo
			fos.write(dados);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			System.out.println("Erro : " + e.getMessage());
		}
	}
}
