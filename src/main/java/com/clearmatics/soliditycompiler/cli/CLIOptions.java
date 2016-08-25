package main.java.com.clearmatics.soliditycompiler.cli;

import org.apache.commons.cli.*;

public class CLIOptions {
    private Options options;
    private CommandLine cmd;

    public CLIOptions() {
        options = new Options();
        options.addOption("i", "input",        true,  "solidity input filename");
        options.addOption("t", "target",       true,  "target language to [trans]compile to");
        options.addOption("h", "help",         false, "print the help info");
        options.addOption("d", "debug",        false, "print debug output");
    }

    public void printUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("solidity [options]", options);
    }

    public boolean processOptions(String[] args) {
        CommandLineParser parser = new PosixParser();
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public int argCount() {
        return cmd.getOptions().length;
    }

    public boolean flagHelp() {
        return cmd.hasOption("h");
    }

    public boolean flagDebug() {
        return cmd.hasOption("d");
    }

    public String flagInputFileName() {
        if (cmd.hasOption("i")) {
            return cmd.getOptionValue("i");
        } else {
            return null;
        }
    }

    public String flagTarget() {
        if (cmd.hasOption("t")) {
            return cmd.getOptionValue("t");
        } else {
            return null;
        }
    }
}
