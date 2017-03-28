package br.com.sysm.jasper.report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;

public class PessoasReportCompiler {

	public static void main(String[] args) throws Exception {
		long inicio = System.currentTimeMillis();
		System.out.println("Conectando ao Banco de Dados");
		Class.forName("com.mysql.jdbc.Driver");
		Connection mySQLConncetion = DriverManager.getConnection("jdbc:mysql://localhost/jasperreports", "jasper", "jasper");
		InputStream relatorioSource = PessoasReport.class.getResourceAsStream("Pessoas.jrxml");
		ByteArrayOutputStream relatorioOutputCompiled = new ByteArrayOutputStream();
		System.out.println("Compilando relatório...");
		JasperCompileManager.compileReportToStream(relatorioSource, relatorioOutputCompiled);
		byte[] compiledReportData = relatorioOutputCompiled.toByteArray();
		relatorioOutputCompiled.close();
		
		
		Map<String, Object> parameters = new LinkedHashMap<String, Object>();
		System.out.println("Gerando relatório...");
		JasperPrint jp = JasperFillManager.fillReport(new ByteArrayInputStream(compiledReportData), parameters, mySQLConncetion);
		JRExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, "meuRaltorioJasperPessoasCompiled.pdf");
		System.out.println("Gerando PDF...");
		exporter.exportReport();
		System.out.println("Concluido com sucesso!");
		System.out.println("Gerado ! em " + (System.currentTimeMillis() - inicio) + " ms.");
	}
}
