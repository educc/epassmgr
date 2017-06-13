package com.epassmgr

/**
 * Created by USER on 12/06/2017.
 */


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option.Builder;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;

class AppOptions {

    public val OPT_HELP = "h"
    public val OPT_ENC = "encrypt"
    public val OPT_DECRYPT = "decrypt"
    public val OPT_KEY = "k"
    public val OPT_LIST = "ls"
    public val OPT_FILE = "file"
    public val OPT_VERSION = "v"

    fun get():Options {
        val commandLine: CommandLine

        val optFilename = Option.builder(OPT_FILE)
                .required(true)
                .desc("A plain or encrypted file")
                .hasArg()
                .build()

        val optEncrypt = Option.builder(OPT_ENC)
                .required(false)
                .desc("Use this option for encrypting a plain text file")
                .build()

        val optDecrypt = Option.builder(OPT_DECRYPT)
                .required(false)
                .desc("Use this option for decrypt a encrypted text file")
                .build()

        val optHelp = Option.builder(OPT_HELP)
                .required(false)
                .desc("Show a help")
                .longOpt("help")
                .build()

        val optKey = Option.builder(OPT_KEY)
                .required(false)
                .desc("Find a key")
                .hasArg()
                .longOpt("findKey")
                .build()

        val optLs = Option.builder(OPT_LIST)
                .required(false)
                .desc("List all keys, doesn't show password")
                .longOpt("listAll")
                .build()

        val optVersion = Option.builder(OPT_VERSION)
                .required(false)
                .desc("Show the version of this program")
                .longOpt("version")
                .build()

        val options = Options()
        options.addOption(optFilename)
        options.addOption(optEncrypt)
        options.addOption(optDecrypt)
        options.addOption(optHelp)
        options.addOption(optKey)
        options.addOption(optLs)
        options.addOption(optVersion)

        return options
    }
}