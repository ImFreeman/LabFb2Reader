package com.example.labaparser;

import com.example.parser.fb2.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class MainWindowController {

    public class TextHolder
    {
        public String Text;
    }
    @FXML
    private Label BookName;

    @FXML
    private TextArea MainTextArea;

    @FXML
    private Button SaveButton;

    @FXML
    private Button SearchButton;

    private FictionBook _book;
    private String _bookName;
    private TextHolder _parsedBook = new TextHolder();
    private TextHolder _metadata = new TextHolder();

    public void Init(FictionBook book, String bookName, String metadata)
    {
        _book = book;
        _bookName = bookName;
        _parsedBook.Text = "";
        _metadata.Text = metadata;

        Body body = _book.getBody();
        if(body.getTitle() != null)
        {
            ParseTitleString(_parsedBook, body.getTitle());
        }
        if(body.getSections() != null)
        {
            for(Section section : body.getSections())
            {
                ParseSectionString(_parsedBook, section);
            }
        }

        BookName.setText(bookName);
        MainTextArea.setText(_parsedBook.Text);
    }

    @FXML
    void OnSaveClickHandler(MouseEvent event)
    {
        try
        {
            Node source=  (Node) event.getSource();
            Stage privaryStage = (Stage) source.getScene().getWindow();
            DirectoryChooser directoryChooser = new DirectoryChooser ();
            directoryChooser.setTitle("Выберите директорию для сохранения");
            directoryChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            File textFile = new File(directoryChooser.showDialog(privaryStage), _bookName + "text.txt") ;
            textFile.createNewFile();
            FileWriter writer = new FileWriter(textFile, false);
            writer.write(_parsedBook.Text);
            writer.flush();
        }
        catch (Exception exception)
        {

        }

    }

    @FXML
    void OnSearchClickClick(MouseEvent event)
    {
        try
        {
            Node source = (Node) event.getSource();
            Stage privaryStage = (Stage) source.getScene().getWindow();
            DirectoryChooser directoryChooser = new DirectoryChooser ();
            directoryChooser.setTitle("Выберите директорию для сохранения");
            directoryChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            File jsonFile = new File(directoryChooser.showDialog(privaryStage), _bookName + "meta.json") ;
            jsonFile.createNewFile();
            FileWriter writer = new FileWriter(jsonFile, false);
            writer.write(_metadata.Text);
            writer.flush();
        }
        catch (Exception exception)
        {

        }
    }

    protected void ParseSectionString(TextHolder str, Section section)
    {
        if(section.getTitle() != null)
        {
            ParseTitleString(str, section.getTitle());
        }
        if(section.getSections() != null)
        {
            for(Section subSection : section.getSections())
            {
                ParseSectionString(str, subSection);
            }
        }
        if(section.getElements() != null)
        {
            ParseElementString(str, section.getElements());
        }
    }

    protected void ParseTitleString(TextHolder writer, Title title)
    {
        try
        {
            for(Element titleValue : title.getParagraphs())
            {
                writer.Text += titleValue.getText() + "\n";
            }
        }
        catch (Exception exception) {}
    }

    protected void ParseElementString(TextHolder writer, List<Element> elements)
    {
        try
        {
            for(Element elem : elements)
            {
                writer.Text += elem.getText() + "\n";
            }
        }
        catch (Exception exception)
        {

        }
    }

    protected void ParseSection(FileWriter writer, Section section)
    {
        if(section.getTitle() != null)
        {
            ParseTitle(writer, section.getTitle());
        }
        if(section.getSections() != null)
        {
            for(Section subSection : section.getSections())
            {
                ParseSection(writer, subSection);
            }
        }
        if(section.getElements() != null)
        {
            ParseElement(writer, section.getElements());
        }
    }

    protected void ParseTitle(FileWriter writer, Title title)
    {
        try
        {
            for(Element titleValue : title.getParagraphs())
            {
                writer.append(titleValue.getText() + "\n");
            }
        }
        catch (Exception exception) {}
    }

    protected void ParseElement(FileWriter writer, List<Element> elements)
    {
        try
        {
            for(Element elem : elements)
            {
                writer.append(elem.getText() + "\n");
            }
        }
        catch (Exception exception)
        {

        }
    }

}
