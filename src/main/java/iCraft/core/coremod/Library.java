package iCraft.core.coremod;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.common.versioning.ComparableVersion;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.mutable.MutableObject;

public class Library {
   public static void init(File mcDir, String mcVersion) {
      File destination = new File(new File(mcDir, "mods"), "iCraft");
      if (!destination.exists()) {
         destination.mkdir();
      }

      if (destination.exists() && destination.isDirectory()) {
         try {
            extractFiles(mcDir, mcVersion, destination);
         } catch (Exception var5) {
            throw new RuntimeException("library/mod extraction failed", var5);
         }

         try {
            loadFiles(destination);
         } catch (Exception var4) {
            throw new RuntimeException("library loading failed", var4);
         }
      } else {
         throw new RuntimeException("can't create mods/iCraft dir");
      }
   }

   private static void extractFiles(File mcDir, String mcVersion, File destination) throws URISyntaxException {
      URL location = Library.class.getProtectionDomain().getCodeSource().getLocation();
      String protocol = location.getProtocol();
      Set<String> validLibFiles = new HashSet();
      int len;
      File dstFile;
      if (protocol.equals("file")) {
         File source = new File(location.toURI());

         for (len = Library.class.getPackage().getName().replaceAll("[^\\.]", "").length() + 1; len >= 0; --len) {
            source = source.getParentFile();
         }

         File[] files = (new File(source, "lib")).listFiles();
         File[] arr;
         int i;
         File srcFile;
         if (files == null) {
            ICraftCoreMod.log.warn("The iCraft/lib directory doesn't exist.");
         } else {
            arr = files;
            len = files.length;

            for (i = 0; i < len; ++i) {
               srcFile = arr[i];
               dstFile = new File(destination, srcFile.getName());
               if (!dstFile.exists() || dstFile.length() != srcFile.length()) {
                  try {
                     FileUtils.copyFile(srcFile, dstFile);
                  } catch (IOException e) {
                     throw new RuntimeException(e);
                  }
                  ICraftCoreMod.log.info("Extracted library " + srcFile.getName() + ".");
               }

               validLibFiles.add(srcFile.getName());
            }
         }

         files = (new File(source, "mod")).listFiles();
         if (files == null) {
            ICraftCoreMod.log.warn("The iCraft/mod directory doesn't exist.");
         } else {
            arr = files;
            len = files.length;

            for (i = 0; i < len; ++i) {
               srcFile = arr[i];
               dstFile = prepareModExtraction(mcDir, mcVersion, srcFile.getName());
               if (dstFile != null) {
                  try {
                     FileUtils.copyFile(srcFile, dstFile);
                  } catch (IOException e) {
                     throw new RuntimeException(e);
                  }
                  ICraftCoreMod.log.info("Extracted mod " + srcFile.getName() + ".");
               }

               validLibFiles.add(srcFile.getName());
            }
         }
      } else {
         if (!protocol.equals("jar")) {
            throw new RuntimeException("invalid protocol (" + location + ").");
         }

         JarFile source = null;
         try {
            source = ((JarURLConnection) location.openConnection()).getJarFile();
         } catch (IOException e) {
            throw new RuntimeException(e);
         }
         Enumeration e = source.entries();

         label76:
         while (true) {
            while (true) {
               JarEntry entry;
               String name;
               do {
                  if (!e.hasMoreElements()) {
                     break label76;
                  }

                  entry = (JarEntry) e.nextElement();
                  name = entry.getName();
               } while (entry.isDirectory());

               String path = FilenameUtils.getPathNoEndSeparator(name);
               String fileName;
               if (path.equals("lib")) {
                  fileName = FilenameUtils.getName(name);
                  dstFile = new File(destination, fileName);
                  if (!dstFile.exists() || dstFile.length() != entry.getSize()) {
                     try {
                        FileUtils.copyInputStreamToFile(source.getInputStream(entry), dstFile);
                     } catch (IOException ex) {
                        throw new RuntimeException(ex);
                     }
                     ICraftCoreMod.log.info("Extracted library " + fileName + ".");
                  }

                  validLibFiles.add(fileName);
               } else if (path.equals("mod")) {
                  fileName = FilenameUtils.getName(name);
                  dstFile = prepareModExtraction(mcDir, mcVersion, fileName);
                  if (dstFile != null) {
                     try {
                        FileUtils.copyInputStreamToFile(source.getInputStream(entry), dstFile);
                     } catch (IOException ex) {
                        throw new RuntimeException(ex);
                     }
                     ICraftCoreMod.log.info("Extracted mod " + fileName + ".");
                  }
               }
            }
         }
      }

      File[] arr = destination.listFiles();
      len = arr.length;

      for (int i = 0; i < len; ++i) {
         File file = arr[i];
         if (!validLibFiles.contains(file.getName())) {
            if (file.delete()) {
               ICraftCoreMod.log.info("Removed old library " + file.getName() + ".");
            } else {
               ICraftCoreMod.log.warn("Can't remove old library " + file.getName() + ".");
            }
         }
      }

   }

   private static void loadFiles(File dir) throws MalformedURLException {
      LaunchClassLoader classLoader = (LaunchClassLoader) Library.class.getClassLoader();
      File[] files = dir.listFiles();
      if (files == null) {
         ICraftCoreMod.log.warn("The directory " + dir + " doesn't exist, can't load libraries.");
      } else {
         File[] arr = files;
         int len = files.length;

         for (int i = 0; i < len; ++i) {
            File file = arr[i];
            classLoader.addURL(file.toURI().toURL());
            ICraftCoreMod.log.info("Loaded library " + file.getName() + ".");
         }
      }

   }

   private static String[] splitVersion(String str) {
      for (int i = 0; i < str.length(); ++i) {
         char c = str.charAt(i);
         if (!Character.isLetter(c)) {
            String[] ret = new String[]{str.substring(0, i), Character.isDigit(c) ? str.substring(i) : str.substring(i + 1)};
            return ret;
         }
      }

      return null;
   }

   private static File prepareModExtraction(File mcDir, String mcVersion, String name) {
      String[] nameParts = splitVersion(FilenameUtils.getBaseName(name));
      if (nameParts == null) {
         throw new RuntimeException("invalid bundled mod filename: " + name);
      } else {
         File modsDir = new File(mcDir, "mods");
         File modsVersionDir = new File(modsDir, mcVersion);
         if (!modsVersionDir.exists()) {
            modsVersionDir.mkdir();
         }

         String prefix = nameParts[0].toLowerCase();
         ComparableVersion version = new ComparableVersion(nameParts[1]);
         MutableObject<File> oldFile = new MutableObject();
         boolean inModsDir = checkDestination(modsDir, prefix, name, version, oldFile);
         boolean inModsVersionDir = checkDestination(modsVersionDir, prefix, name, version, oldFile);
         if (!inModsDir && !inModsVersionDir) {
            if (oldFile.getValue() != null) {
               if (((File) oldFile.getValue()).delete()) {
                  ICraftCoreMod.log.info("Removed old mod " + ((File) oldFile.getValue()).getName());
               } else {
                  ICraftCoreMod.log.warn("Can't remove old mod " + ((File) oldFile.getValue()).getName());
               }
            }

            return new File(modsVersionDir, name);
         } else {
            return null;
         }
      }
   }

   private static boolean checkDestination(File destination, final String prefix, String name, ComparableVersion newVersion, MutableObject<File> oldFile) {
      boolean found = false;
      File[] arr = destination.listFiles(new FileFilter() {
         public boolean accept(File file) {
            return !file.isDirectory() && file.getName().toLowerCase().startsWith(prefix);
         }
      });
      int len = arr.length;

      for (int i = 0; i < len; ++i) {
         File dstFile = arr[i];
         if (found) {
            return true;
         }

         found = true;
         if (dstFile.getName().equalsIgnoreCase(name)) {
            return true;
         }

         String[] dstNameParts = splitVersion(FilenameUtils.getBaseName(dstFile.getName()));
         if (dstNameParts == null) {
            return true;
         }

         ComparableVersion dstVersion = new ComparableVersion(dstNameParts[1]);
         if (dstVersion.compareTo(newVersion) >= 0) {
            return true;
         }

         if (oldFile.getValue() != null) {
            return true;
         }

         oldFile.setValue(dstFile);
      }

      return false;
   }
}
