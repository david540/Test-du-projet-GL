package fr.ensimag.deca.tree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;

public class BashTester {

	private String codeAssembleur;
	private String expectedOutput;

	public BashTester(String codeAssembleur, String expectedOutput) {
		this.codeAssembleur = codeAssembleur;
		this.expectedOutput = expectedOutput;
	}

	public boolean executeTest() throws IOException {

	    File tempScript = createTempScript();
	    boolean flag = false;
	    try {
	        ProcessBuilder pb = new ProcessBuilder("bash", tempScript.toString());
	        Process process = pb.start();
	        BufferedReader reader = new BufferedReader(
	        		new InputStreamReader(process.getInputStream()));

	        process.waitFor();

	        String s;
	        String totalOutput = "";
	        while ((s = reader.readLine()) != null) {
	          totalOutput += s + "\n";
	        }
	        flag = totalOutput.equals(this.expectedOutput);

	    } catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
	        tempScript.delete();
	    }
	    return flag;
	}

	public File createTempScript() throws IOException {
	    File tempScript = File.createTempFile("script", null);

	    Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
	            tempScript));
	    PrintWriter printWriter = new PrintWriter(streamWriter);

	    printWriter.println("#!/bin/bash");
	    this.codeAssembleur = this.codeAssembleur.replace("\"", "\\\"");
	    printWriter.println("echo \""+this.codeAssembleur + "\" > output.txt");
	    printWriter.println("ima output.txt");
	    printWriter.println("rm output.txt");

	    printWriter.close();

	    return tempScript;
	}
}
