package fuz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class Regras {

    public Regras() {
        // TODO Auto-generated constructor stub
    }

    /**    
     * @param args
     */
    
    
    public static String[][] leArquivos() throws IOException{
        File file = new File("src/fuz/regras.csv"); 
        
        if (! file.exists()) { 
            return null; 
        } 
        else{           
            BufferedReader br = new BufferedReader(new FileReader("src/fuz/regras.csv")); 
            String linha;
                      
            int cont=0;
            while((linha = br.readLine()) != null ){               
                cont++;
                
            }           
            
            String[] lines;
            String[][] linesCsv = new String[cont][cont];
            int i=0;
            br =new BufferedReader(new FileReader("src/fuz/regras.csv"));
            while((linha = br.readLine()) != null ){               
                lines = linha.split(";");
                //System.out.println(lines.length);
                       
                for (int j=0; j<lines.length; j++) {
                	//System.out.println("."+lines[j]);
                	linesCsv[i][j] = lines[j];
                    //System.out.println("."+lines[j]);
                }
                i++;
            }       
            
            
            br.close();
            return linesCsv;    
        }
        
    }


    public static String makeArq(String[][] linesCsv){
    	String texto="";
    	int cont = linesCsv.length;
        for(int k=1;k<cont;k++){
        	for(int m=1;m<cont;m++){
        		if(linesCsv[k][m]!="-" && linesCsv[0][m]!="-" && linesCsv[m][0]!="-")
        			texto=texto+"if direcao is "+linesCsv[k][0]+" and posicao is "+linesCsv[0][m]+" then angulo is "+linesCsv[k][m]+" \n" ;
        	}
        }
        return texto;
    }
    
    
    public static void main(String[] args) {
        
        // TODO Auto-generated method stub
        try {
            String[][] str= Regras.leArquivos();
            System.out.println(Regras.makeArq(str));
//            System.out.println(str[0].length);
//            for(int i=0;i<str.length;i++)
//            	for(int j=0;j<str[i].length;j++)
//            		System.out.println("a["+i+","+j+"]:"+str[i][j]);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

