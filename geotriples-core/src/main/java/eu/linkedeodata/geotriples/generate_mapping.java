package eu.linkedeodata.geotriples;

import java.io.PrintStream;

import jena.cmdline.ArgDecl;
import jena.cmdline.CommandLine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.d2rq.mapgen.MappingGenerator;
import org.geotools.gml3.Circle.Arc;

import be.ugent.mmlab.rml.mapgen.CSVMappingGenerator;
//import be.ugent.mmlab.rml.mapgen.SQLMappingGenerator;
import be.ugent.mmlab.rml.mapgen.ShapefileMappingGenerator;
import be.ugent.mmlab.rml.mapgen.XMLMappingGeneratorTrans;
import eu.linkedeodata.geotriples.gui.RecipeMapping;

/**
 * Command line interface for {@link MappingGenerator}.
 * 
 * @author Richard Cyganiak (richard@cyganiak.de)
 */
public class generate_mapping {
	private final static Logger log = LoggerFactory.getLogger(generate_mapping.class);
	private RecipeMapping recipe = null;
	protected java.io.PrintStream guiOutput = null;

	public static void main(String[] args) throws Exception {
		// invoke the appropriate generate mapping
		new generate_mapping().process(args);
	}

	public generate_mapping(RecipeMapping receipt, PrintStream guiOutput) {
		this.recipe = receipt; // set the receipt for mapping
		this.guiOutput = guiOutput;
	}

	public generate_mapping() {
		// do nothing
	}

	public void process(String[] args) throws Exception {
		if (args.length >= 1) {
			String lastToken = args[args.length - 1];
			if(lastToken.equalsIgnoreCase("help") || lastToken.equalsIgnoreCase("-h")){
				generate_mapping_usage();
				System.exit(0);
			}
			
			// if (lastToken.endsWith(".kml")) {
			// log.info("KML detected for processing");
			// //System.out.println("Currently KML is not implemented within WP2
			// (soon)");
			// (new
			// eu.linkedeodata.geotriples.kml.generate_mapping(this.recipe,this.guiOutput)).process(args);
			// }
			// else
			if (lastToken.endsWith(".shp")) {
				log.info("Shapefile detected for processing");
				boolean isRML = false;
				// test if user want to generate as RML mapping
				for (String arg : args) {
					if (arg.equals("-rml")) {
						isRML = true;
						break;
					}
				}
				if (isRML) {
					final ArgDecl outfileArg = new ArgDecl(true, "o", "out", "outfile");
					final ArgDecl baseIRIArg = new ArgDecl(true, "b", "base", "Base IRI");
					final ArgDecl ontologyOutArg = new ArgDecl(true, "ont", "onto", "outfile for ontology");
					final ArgDecl isRMLarg = new ArgDecl(false, "-rml");
					final CommandLine cmd = new CommandLine();
					cmd.add(baseIRIArg);
					cmd.add(outfileArg);
					cmd.add(ontologyOutArg);
					cmd.add(isRMLarg);
					cmd.process(args);
					new ShapefileMappingGenerator(lastToken, cmd.getValue(outfileArg), cmd.getValue(baseIRIArg),
							cmd.getValue(ontologyOutArg)).run();

				} else {
					if (this.recipe != null) {
						(new eu.linkedeodata.geotriples.shapefile.generate_mapping(this.recipe, this.guiOutput))
								.process(args, this.recipe);
					} else {
						(new eu.linkedeodata.geotriples.shapefile.generate_mapping(this.recipe, this.guiOutput))
								.process(args);
					}
				}
			} else if (lastToken.endsWith(".pdf")) {
				log.info("GeoPDF detected for processing");
				System.out.println("Currently GeoPDF is not implemented within WP2 (soon)");
			}else if(lastToken.endsWith(".csv")){
				final ArgDecl outfileArg = new ArgDecl(true, "o", "out", "outfile");
				final ArgDecl baseIRIArg = new ArgDecl(true, "b", "base", "Base IRI");
				final ArgDecl ontologyOutArg = new ArgDecl(true, "ont", "onto", "outfile for ontology");
				final ArgDecl isRMLarg = new ArgDecl(false, "-rml");
				final CommandLine cmd = new CommandLine();
				cmd.add(baseIRIArg);
				cmd.add(outfileArg);
				cmd.add(ontologyOutArg);
				cmd.add(isRMLarg);
				cmd.process(args);
				new CSVMappingGenerator(lastToken, cmd.getValue(outfileArg), cmd.getValue(baseIRIArg), cmd.getValue(ontologyOutArg),',').run();;
			}else if(lastToken.endsWith(".tsv")){
				final ArgDecl outfileArg = new ArgDecl(true, "o", "out", "outfile");
				final ArgDecl baseIRIArg = new ArgDecl(true, "b", "base", "Base IRI");
				final ArgDecl ontologyOutArg = new ArgDecl(true, "ont", "onto", "outfile for ontology");
				final ArgDecl isRMLarg = new ArgDecl(false, "-rml");
				final CommandLine cmd = new CommandLine();
				cmd.add(baseIRIArg);
				cmd.add(outfileArg);
				cmd.add(ontologyOutArg);
				cmd.add(isRMLarg);
				cmd.process(args);
				new CSVMappingGenerator(lastToken, cmd.getValue(outfileArg), cmd.getValue(baseIRIArg), cmd.getValue(ontologyOutArg),'\t').run();;
			} else if (lastToken.endsWith(".xml") || lastToken.endsWith(".gml") || lastToken.endsWith(".kml")) {
				log.info("XML detected for processing");
				final ArgDecl outfileArg = new ArgDecl(true, "o", "out", "outfile");
				final ArgDecl xsdifileArg = new ArgDecl(true, "x", "xsd", "XSD file");
				final ArgDecl baseIRIArg = new ArgDecl(true, "b", "base", "Base IRI");
				final ArgDecl rootElement = new ArgDecl(true, "r", "root", "Root element to start");
				final ArgDecl nullTypesArg = new ArgDecl(false, "null", "nulltypes",
						"Allow null types to be adressed as classes (Triples Maps)");
				final ArgDecl ontologyOutArg = new ArgDecl(true, "ont", "onto", "outfile for ontology");
				final ArgDecl rootPathArg = new ArgDecl(true, "rp", "rootpath",
						"path of root element eg /a/b when root element is c");
				final ArgDecl namespacesArg = new ArgDecl(true, "ns", "namespaces",
						"used for namespaces in root path (-rp | --rootpath)");
				final ArgDecl onlyNamespace = new ArgDecl(true, "onlyns", "onlynamespace",
						"only global elements for this namespace will be analyzed");
				final ArgDecl isRML = new ArgDecl(false, "-rml");

				final CommandLine cmd = new CommandLine();
				cmd.add(xsdifileArg);
				cmd.add(baseIRIArg);
				cmd.add(outfileArg);
				cmd.add(nullTypesArg);
				cmd.add(rootElement);
				cmd.add(ontologyOutArg);
				cmd.add(rootPathArg);
				cmd.add(namespacesArg);
				cmd.add(onlyNamespace);
				cmd.add(isRML);
				try {
					cmd.process(args);
				} catch (IllegalArgumentException ex) {
					if (ex.getMessage() == null) {
						ex.printStackTrace(System.err);
					} else {
						System.err.println(ex.getMessage());
					}
					log.info("Command line tool exception", ex);
					System.exit(1);
				}
				String dd = cmd.getValue(ontologyOutArg);
				System.out.println(dd);

				// new XMLMappingGenerator(cmd.getValue(xsdifileArg), lastToken,
				// cmd.getValue(outfileArg),
				// cmd.getValue(baseIRIArg),cmd.getValue(rootElement),cmd.getValue(rootPathArg),cmd.getValue(namespacesArg),
				// cmd.hasArg(nullTypesArg),cmd.getValue(ontologyOutArg),cmd.getValue(onlyNamespace)).run();
				new XMLMappingGeneratorTrans(cmd.getValue(xsdifileArg), lastToken, cmd.getValue(outfileArg),
						cmd.getValue(baseIRIArg), cmd.getValue(rootElement), cmd.getValue(rootPathArg),
						cmd.getValue(namespacesArg), cmd.hasArg(nullTypesArg), cmd.getValue(ontologyOutArg),
						cmd.getValue(onlyNamespace)).run();
			} else {
				boolean isRML = false;
				log.info("Database detected for processing");
				for (String arg : args) {
					if (arg.equals("-rml")) {
						isRML = true;
						break;
					}
				}
				if (isRML) {
					final ArgDecl outfileArg = new ArgDecl(true, "o", "out", "outfile");
					final ArgDecl baseIRIArg = new ArgDecl(true, "b", "base", "Base IRI");
					final ArgDecl ontologyOutArg = new ArgDecl(true, "ont", "onto", "outfile for ontology");
					final ArgDecl usernameArg = new ArgDecl(true, "u", "username", "username");
					final ArgDecl passwordArg = new ArgDecl(true, "p", "password", "password");
					final ArgDecl isRMLarg = new ArgDecl(false, "-rml");
					final ArgDecl onlytableOutArg = new ArgDecl(true, "table");
					final CommandLine cmd = new CommandLine();
					cmd.add(baseIRIArg);
					cmd.add(outfileArg);
					cmd.add(ontologyOutArg);
					cmd.add(isRMLarg);
					cmd.add(usernameArg);
					cmd.add(passwordArg);
					cmd.add(onlytableOutArg);
					cmd.process(args);
					System.out.println("Please uncomment this line 195 in generate_mapping.java in geotriples-main project");
					//new SQLMappingGenerator(lastToken, cmd.getValue(outfileArg), cmd.getValue(baseIRIArg), cmd.getValue(usernameArg), cmd.getValue(passwordArg), cmd.getValue(ontologyOutArg),cmd.getValue(onlytableOutArg)).run();;
				} else {
					(new d2rq.generate_mapping()).process(args);
				}
			}
		} else {
			usage();
		}
	}

	private void generate_mapping_usage() {
		System.err.println(".___________________________________________________________.");
		System.err.println("|\\._______________________________________________________./|");
		System.err.println("|\\|                       GeoTriples                      |/|");
		System.err.println("|\\|  a tool for transforming EO/geospatial data into RDF  |/|");
		System.err.println(" \\._______________________________________________________./ ");

		System.err.println();
		System.err.println("Usage for generate mapping:  [options] <source>");
		System.err.println("\tOptions:");
		System.err.println("\t\t-o outfile\t\tOutput mapping file name (default: stdout)");
		System.err.println("\t\t-b base IRI\t\te.g., http://data.linkedeodata.eu/mydataset");
		System.err.println("\tDatabase options:");
		System.err.println("\t\t-u username\t\tDatabase Username (Optional)");
		System.err.println("\t\t-p password\t\tDatabase Password (Optional)");
		System.err.println();
		System.err.println("\tArguments:");
		System.err.println("\t\tThe path to the source file. Can be Shapefile, XML, JSON, CSV or Relational Database JDBC URL");
	}

	public static void usage() {
		System.err.println("usage: generate-mapping [options] inputFileURL");
		System.err.println();
		System.err.println("  Options:");
		System.err.println("    -o outfile.ttl  Output file name (default: stdout)");
		System.err.println("    -v              Generate RDFS+OWL vocabulary instead of mapping file");
		System.err.println("    --verbose       Print debug information");
		System.err.println("    -b <baseIR> e.g. http://geo.linkedopendata.gr/natura");
		System.err.println("	-geov			Use target geospatial vocabulary (GeoSPARQL/stRDF)");
		System.err.println();
		System.exit(1);
	}

}
