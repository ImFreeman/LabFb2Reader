package com.example.labaparser;

import com.example.json.JSONArray;
import com.example.json.JSONObject;
import com.example.parser.fb2.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HelloController {
    @FXML
    private Label welcomeText;

    private File fileObject;

    @FXML
    protected void onHelloButtonClick(ActionEvent event)
    {
        Node source=  (Node) event.getSource();
        Stage privaryStage = (Stage) source.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter fb2Filter = new FileChooser.ExtensionFilter(".fb2 files (*.fb2)", "*.fb2");
        fileChooser.getExtensionFilters().add(fb2Filter);
        fileChooser.getExtensionFilters().addAll(fb2Filter);
        fileChooser.setTitle("Выбор файла");

        fileObject = fileChooser.showOpenDialog(privaryStage);

        if(fileObject == null)
        {
            return;
        }

        FictionBook fb2;
        Map<String, Object> metaData = new HashMap<String, Object>();
        String jsonString;

        try
        {
            fb2 = new FictionBook(fileObject);

            Description description = fb2.getDescription();
            TitleInfo titleInfo = description.getTitleInfo();

            metaData.put("genre", titleInfo.getGenres());
            metaData.put("keywords", titleInfo.getKeywords());
            metaData.put("date", titleInfo.getDate());
            metaData.put("lang", titleInfo.getLang());
            metaData.put("srcLang", titleInfo.getSrcLang());

            ArrayList<Person> authorsPerson = titleInfo.getAuthors();
            JSONArray currentAuthor = new JSONArray();
            for (Person person : authorsPerson)
            {
                currentAuthor.add(person.getFirstName());
                currentAuthor.add(person.getMiddleName());
                currentAuthor.add(person.getLastName());
                currentAuthor.add(person.getNickname());
                currentAuthor.add(person.getHomePages());
                currentAuthor.add(person.getEmails());
            }
            metaData.put("authors", currentAuthor);

            ArrayList<Person> translatorsPerson = titleInfo.getTranslators();
            JSONArray translators = new JSONArray();
            for (Person person : translatorsPerson)
            {
                translators.add(person.getFirstName());
                translators.add(person.getMiddleName());
                translators.add(person.getLastName());
                translators.add(person.getNickname());
                translators.add(person.getHomePages());
                translators.add(person.getEmails());
            }
            metaData.put("translators", translators);
            if(titleInfo.getAnnotation() != null)
            {
                metaData.put("annotation_lang", titleInfo.getAnnotation().getLang());

                ArrayList<Element> annotationElems = titleInfo.getAnnotation().getElements();
                String annotationText = "";
                for(Element elem : annotationElems)
                {
                    annotationText += elem.getText();
                }
                metaData.put("annotation_text", annotationText);
            }


            if(titleInfo.getSequence() != null)
            {
                metaData.put("titleInfo_sequence_name", titleInfo.getSequence().getName());
                metaData.put("titleInfo_sequence_number", titleInfo.getSequence().getNumber());
            }

            PublishInfo publishInfo = description.getPublishInfo();
            metaData.put("bookName", publishInfo.getBookName());
            metaData.put("city", publishInfo.getCity());
            metaData.put("year", publishInfo.getYear());
            metaData.put("publisher", publishInfo.getPublisher());
            metaData.put("isbn", publishInfo.getIsbn());

            if(publishInfo.getSequence() != null)
            {
                metaData.put("publishInfo_sequence_name", publishInfo.getSequence().getName());
                metaData.put("publishInfo_sequence_number", publishInfo.getSequence().getNumber());
            }

            jsonString = new JSONObject(metaData).toJSONString();
            LocalDate date = LocalDate.now();
            String pathJson = System.getProperty("user.dir") + "/"+ date + "_" + fileObject.getName() + "meta.json";
            File jsonFile = new File(pathJson);
            jsonFile.createNewFile();
            try(FileWriter writer = new FileWriter(jsonFile, false))
            {
                writer.write(jsonString);
                writer.flush();
            }



            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("mainWindow.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene(new Scene(fxmlLoader.load()));

            MainWindowController mainWindowController = fxmlLoader.getController();
            mainWindowController.Init(fb2, fileObject.getName(), jsonString);

            stage.show();

            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("popup.fxml"));
            stage = new Stage(StageStyle.DECORATED);
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();

        }
        catch (ParserConfigurationException | IOException | SAXException e)
        {
            e.printStackTrace();
        }

    }
}