/*
package eina.unizar.melodiaapp.Modules;

import android.os.Build;

import java.io.ByteArrayInputStream;
import java.io.File;
import android.util.Base64;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
*/

/*
 * Módulo que contiene una clase con los métodos necesarios para convertir strings en arrays de bytes
 * y después en archivos de audio y viceversa.
 */

/*
public class AudioConversions {
    private String binaryString;
    private byte[] byteArray;
    private File inputFile;

    // Constructor, si se quiere conseguir un archivo de audio a partir de un string en file pasar null
    // Si se quiere conseguir un string en base64 a partir de un archivo de audio pasar null en el string
    public void JSONConversions(String binaryString, File file) {
        this.binaryString = binaryString;
        this.inputFile = file;
    }

    // Método para convertir un string en un array de bytes
    public void convertStringToByteArray() {
        this.byteArray = android.util.Base64.decode(this.binaryString, android.util.Base64.DEFAULT);
    }

    // Método para convertir un array de bytes en un archivo de audio
    public File convertByteArrayToFile(String fileName) {
        File outputFile = new File(fileName);

        AudioInputStream audioInputStream = new AudioInputStream(new ByteArrayInputStream(byteArray), new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false), byteArray.length / 4);

        AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, outputFile);

        return outputFile;
    }

    // Método para convertir un archivo de audio en un string en base64
    public String convertFileToString() {
        byte[] fileContent = File.readAllBytes(this.inputFile.toPath());
        String encodedString = android.util.Base64.encodeToString(fileContent, android.util.Base64.DEFAULT);

        return encodedString;
    }
}

*/