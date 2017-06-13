/**
 * Created by USER on 8/06/2017.
 */

package com.epassmgr;



import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option.Builder;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import java.io.Console
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import org.apache.commons.cli.HelpFormatter
import java.io.BufferedWriter
import java.nio.file.Path
import java.util.*


class App {

    val INIT_VECTOR:String = "HD7UiIAY5y1rqZQu"
    val console:Console? = System.console();
    val version:String = "1.0"
    val release:String = "12 jun 2017"


    companion object {
        @JvmStatic fun main(args: Array<String>) {
            App().run(args)
        }
    }

    fun run(args: Array<String>) {
        val commandLine: CommandLine
        val parser = DefaultParser()
        val appOptions = AppOptions()
        val formatter = HelpFormatter()

        try {
            commandLine = parser.parse(appOptions.get(), args)
            val filename:String = commandLine.getOptionValue(appOptions.OPT_FILE)

            val pfile = Paths.get(filename)
            if( !Files.exists(pfile) || pfile == null ){
                println("The file ${filename} doesn't exist")
                return;
            }

            if (commandLine.hasOption(appOptions.OPT_ENC)) {
                encryptFile(pfile)
            }else if (commandLine.hasOption(appOptions.OPT_DECRYPT)) {
                decryptFile(pfile)
            }else if(commandLine.hasOption(appOptions.OPT_HELP)){
                formatter.printHelp("epassmgr", appOptions.get())
            }else if (commandLine.hasOption(appOptions.OPT_VERSION)) {
                println("Version: ${version}")
                println("Release: ${release}")
            }else if(commandLine.hasOption(appOptions.OPT_LIST)){
                val key = readKey()

                val passmgr = PassManager(key, filename)
                var i = 1
                for (item:ItemPass in passmgr.listAll()){
                    println("${i}. name=${item.name}   username=${item.username}")
                    i++
                }
            }else if(commandLine.hasOption(appOptions.OPT_KEY)){
                val passmgr = PassManager(readKey(), filename)
                val name = commandLine.getOptionValue(appOptions.OPT_KEY)

                val item = passmgr.find(name)
                if( item != null){
                    println("Username:")
                    println(item.username)
                    println("Password:")
                    println(item.password)
                }else{
                    println("Doesn't exists ${name} in your passmanager")
                }

            }else{
                println("without params")
            }

        } catch (exception: ParseException) {
            print("Parse error: ")
            println(exception.message)

            formatter.printHelp("epassmgr", appOptions.get())
        } catch (ex:AppException){
            println(ex.message)
        }
    }

    fun readKey(text:String = "key (16 chars): "):String{
        if( console == null) return ""

        println(text);
        val key1  = console.readPassword();
        val str = String(key1)
        if( str.length != 16){
            throw AppException("Key should has 16 chars")
        }
        return str
    }

    fun encryptFile(pfile:Path){
        if( console == null) return

        println ("Encrypting file")

        val key1 = readKey()
        val key2 = readKey("Verify password: ")
        if(  key1 != key2){
            println("Passwords doesn't match")
            return
        }

        val bytes = Files.readAllBytes(pfile)
        val content:String = Encryptor.encrypt(key1, INIT_VECTOR, bytes)

        val fileResult = pfile.toFile().name + ".enc"
        File(fileResult).printWriter().use { out ->
            out.println(content)

            println("New file was created: ${fileResult}")
        }
    }


    fun decryptFile(pfile:Path){
        if( console == null) return
        val key = readKey()

        val bytes = Files.readAllBytes(pfile)
        val content:String = Encryptor.decrypt(key, INIT_VECTOR, bytes)

        val fileResult = pfile.toFile().name + ".plain.txt"
        File(fileResult).printWriter().use { out ->
            out.println(content)
            println("New file was created: ${fileResult}")
        }
    }
}