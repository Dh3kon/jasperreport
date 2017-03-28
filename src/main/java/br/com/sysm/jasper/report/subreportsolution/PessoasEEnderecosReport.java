package br.com.sysm.jasper.report.subreportsolution;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedHashMap;
import java.util.Map;

import br.com.sysm.jasper.report.PessoasReport;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;

public class PessoasEEnderecosReport {

	public static void main(String[] args) throws Exception {
		long inicio = System.currentTimeMillis();
		System.out.println("Conectando ao Banco de Dados");
		Class.forName("com.mysql.jdbc.Driver");
		Connection mySQLConncetion = DriverManager.getConnection("jdbc:mysql://localhost/jasperreports", "jasper", "jasper");
		InputStream relatorioSource = PessoasReport.class.getResourceAsStream("Pessoas.jrxml");
		System.out.println("Compilando relatório...");
		InputStream theReport = compileReport();
		
		
		
		
		Map<String, Object> parameters = new LinkedHashMap<String, Object>();
		System.out.println("Gerando relatório...");
		JasperPrint jp = JasperFillManager.fillReport(theReport, parameters, mySQLConncetion);
		JRExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, "meuRelatorioJasperPessoasEnderecosCompiled.pdf");
		System.out.println("Gerando PDF...");
		exporter.exportReport();
		System.out.println("Concluido com sucesso!");
		System.out.println("Gerado ! em " + (System.currentTimeMillis() - inicio) + " ms.");
	}
	
	public static InputStream compileReport() throws Exception {

		System.out.println("Compilando conta corrente...");
		InputStream isPCC = PessoasEEnderecosReport.class.getResourceAsStream("PessoasEEnderecos_ContaCorrente.jrxml");
		ByteArrayOutputStream relatorioCCOutputStream = new ByteArrayOutputStream();
		JasperCompileManager.compileReportToStream(isPCC, relatorioCCOutputStream);
		byte[] compiledCCReportData = relatorioCCOutputStream.toByteArray();
		FileOutputStream filePCC = new FileOutputStream("PessoasEEnderecos_ContaCorrente.jasper");
		filePCC.write(compiledCCReportData);
		filePCC.close();
		
		System.out.println("Compilando detalhes...");
		InputStream isPD = PessoasEEnderecosReport.class.getResourceAsStream("PessoasEEnderecos_Detalhe.jrxml");
		ByteArrayOutputStream relatorioPDOutputStream = new ByteArrayOutputStream();
		JasperCompileManager.compileReportToStream(isPD, relatorioPDOutputStream);
		byte[] compiledPDReportData = relatorioPDOutputStream.toByteArray();
		FileOutputStream filePD = new FileOutputStream("PessoasEEnderecos_Detalhe.jasper");
		filePD.write(compiledPDReportData);
		filePD.close();
		
		System.out.println("Compilando relatório final...");
		InputStream finalFile = PessoasEEnderecosReport.class.getResourceAsStream("PessoasEEnderecos.jrxml");
		ByteArrayOutputStream relatorioFinalOutputStream = new ByteArrayOutputStream();
		JasperCompileManager.compileReportToStream(finalFile, relatorioFinalOutputStream);
		byte[] compiledFinalReportData = relatorioFinalOutputStream.toByteArray();
		
		return new ByteArrayInputStream(compiledFinalReportData);
	}
}
