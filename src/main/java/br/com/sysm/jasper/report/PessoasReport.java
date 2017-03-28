package br.com.sysm.jasper.report;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;

public class PessoasReport {

	public static void main(String[] args) throws Exception {
		long inicio = System.currentTimeMillis();
		System.out.println("Conectando ao Banco de Dados");
		Class.forName("com.mysql.jdbc.Driver");
		Connection mySQLConncetion = DriverManager.getConnection("jdbc:mysql://localhost/jasperreports", "jasper", "jasper");
		InputStream relatorio = PessoasReport.class.getResourceAsStream("Pessoas.jasper");
		Map<String, Object> parameters = new LinkedHashMap<String, Object>();
		System.out.println("Gerando relatório...");
		JasperPrint jp = JasperFillManager.fillReport(relatorio, parameters, mySQLConncetion);
		JRExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, "meuRaltorioJasperPessoas.pdf");
		System.out.println("Gerando PDF...");
		exporter.exportReport();
		System.out.println("Concluido com sucesso!");
		System.out.println("Gerado em " + (System.currentTimeMillis() - inicio) + "ms.");
		
	}
}
