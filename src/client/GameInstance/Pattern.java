package client.GameInstance;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;


import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Pattern {

    private static final int BOARD_LENGTH = 400;
    private static final int BOARD_DEPTH = 5;
    private static final int BOARD_WIDTH = 5;
    private static PhongMaterial[] gridMat;
    private static int gridDimension;
    private static Group patternGroup;
    private static Box[] gridCell;
    private static int[][] gridMatrix;
    private static Rotate r;
    private static final Transform t = new Rotate();

    public Pattern(int gridDimension)
    {
        this.gridDimension = gridDimension;

        gridMatrix = (new PatternGenerator(gridDimension)).generatePattern(false);

        gridMat = new PhongMaterial[gridDimension*gridDimension];
        //gridMat.setDiffuseMap( new Image(getClass().getResourceAsStream("questionMark.png")));
        gridCell = new Box[(gridDimension) * (gridDimension)];
        patternGroup = createPattern();
    }
    public Group createPatternGroup()
    {
        return patternGroup;
    }

    private Group createPattern()
    {
        Group boardGroupInst = new Group();
        int gridIndex;

        for (int i = 0; i < gridDimension; i++) {
            for (int j = 0; j < gridDimension; j++) {

                gridIndex = i * gridDimension + j;

                gridCell[gridIndex] = new Box(BOARD_LENGTH / gridDimension, BOARD_LENGTH / gridDimension, 0);
                gridMat[gridIndex] = new PhongMaterial();
                gridCell[gridIndex].setId(Integer.toString(gridIndex));
                System.out.println(gridMatrix[gridIndex][0]);
                String png = Integer.toString(gridMatrix[gridIndex][0]) + "NoBorder.png";
                System.out.println(png+ " " + gridMatrix[gridIndex][1]);
                try {
                    gridMat[gridIndex].setDiffuseMap(new Image(new FileInputStream("assets/CubeFaces/" + png)));
                }
                catch(Exception e){
                    System.out.println("File not found");
                }
                gridCell[gridIndex].setMaterial(gridMat[gridIndex]);
                gridCell[gridIndex].getTransforms().add(new Rotate(90*gridMatrix[gridIndex][1], Rotate.Z_AXIS));

                gridCell[gridIndex].translateXProperty().set(((BOARD_LENGTH / gridDimension) * (j + 0.5)) + (-BOARD_LENGTH / 2));
                gridCell[gridIndex].translateYProperty().set(((BOARD_LENGTH / gridDimension) * (i + 0.5)) + (-BOARD_LENGTH / 2));
                boardGroupInst.getChildren().add(gridCell[gridIndex]);
            }
        }
        System.out.println(boardGroupInst);
        return boardGroupInst;
    }

    public boolean checkPattern(ImageView[] boardImages, ImageView[] patternImages){

        if( boardImages.length != patternImages.length)
            return false;


        for( int i = 0 ; i <  boardImages.length && i < patternImages.length; i++ ) {

            BufferedImage boardImage = SwingFXUtils.fromFXImage(boardImages[i].snapshot(null, null), null);
            BufferedImage patternImage = SwingFXUtils.fromFXImage(patternImages[i].snapshot(null, null), null);

            Raster rasterBoard = boardImage.getRaster();
            Raster rasterPattern = patternImage.getRaster();


            for (int x = 0; x < boardImage.getWidth() && x < patternImage.getWidth(); x++) {
                for (int y = 0; y < boardImage.getHeight() && y < patternImage.getHeight(); y++) {
                    int boardR = rasterBoard.getSample(x, y, 0);
                    int boardG = rasterBoard.getSample(x, y, 1);
                    int boardB = rasterBoard.getSample(x, y, 2);

                    int patternR = rasterPattern.getSample(x, y, 0);
                    int patternG = rasterPattern.getSample(x, y, 1);
                    int patternB = rasterPattern.getSample(x, y, 2);


                    if (boardR != patternR || boardG != patternG || boardB != patternB)
                        return false;
                }
            }
        }
                return true;

    }
    public void setMatQuestMark()
    {
        for(int i = 0; i < gridDimension*gridDimension; i++)
        {
            gridMat[i].setDiffuseMap(new Image(getClass().getResourceAsStream("questionMark.png")));
            gridCell[i].setMaterial(gridMat[i]);
        }
    }

    public static int[][] getGridMatrix() {
        return gridMatrix;
    }
}
