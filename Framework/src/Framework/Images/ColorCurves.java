package Framework.Images;

import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.Expression;


public class ColorCurves {

    //instance variables.
    private int rangeStart;
    private int redRangeStart;
    private int greenRangeStart;
    private int blueRangeStart;

    private int rangeEnd;
    private int redRangeEnd;
    private int greenRangeEnd;
    private int blueRangeEnd;

    private String rangeFormula;
    private String redRangeFormula;
    private String greenRangeFormula;
    private String blueRangeFormula;

    //default color curves
    public final static ColorCurves INVERT = new ColorCurves(0,255,"255-a");
    public final static ColorCurves GRAYSCALE = new ColorCurves(0,255,"(r+g+b)/3");

    //constructors
    public ColorCurves(int start,int end, String form)
    {
        rangeStart = start;
        rangeEnd = end;
        rangeFormula = form;
    }


    public ColorCurves()
    {

    }

    //setting formulas and ranges
    public void set(int start, int end, String form)
    {
        rangeStart = start;
        rangeEnd = end;
        rangeFormula = form;
    }

    public void redSet(int start, int end, String form)
    {
        redRangeStart = start;
        redRangeEnd = end;
        redRangeFormula = form;
    }

    public void greenSet(int start, int end, String form)
    {
        greenRangeStart = start;
        greenRangeEnd = end;
        greenRangeFormula = form;
    }

    public void blueSet(int start, int end, String form)
    {
        blueRangeStart = start;
        blueRangeEnd = end;
        blueRangeFormula = form;
    }


    /**
     * return the results of form used on inPixel.
     * @param inPixel the pixel whose values are used.
     * @param form A string that gives the mathematical expression used in evaluation
     * @param color the specific color that is evaluated, if necessary.
     * @return
     */
    public int evaluateAll(Pixel inPixel, String form, String color)
    {
        int output;
        int value;
        switch(color)
        {
            case "red": value = inPixel.getRed();
            break;

            case "green": value = inPixel.getGreen();
            break;

            case "blue": value = inPixel.getBlue();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + color);
        }
        Expression expression = new ExpressionBuilder(form)
                .variables("a","r","g","b")
                .build()
                .setVariable("a", value)
                .setVariable("r", inPixel.getRed())
                .setVariable("g", inPixel.getGreen())
                .setVariable("b", inPixel.getBlue());

        output = (int)expression.evaluate();

        return output;
    }


    /**
     * processes each color value of a pixel individually. If there isn't a specific color formula, use the default formula
     * @param input the Pixel to be processed.
     */
    public void evaluate(Pixel input)
    {
        Pixel postCurve = new Pixel();
        if(redRangeFormula == null)
        {
            if(rangeFormula != null)
            {
                postCurve.setRed(evaluateAll(input, rangeFormula, "red"));
            }
        }
        else
        {
            postCurve.setRed(evaluateAll(input,redRangeFormula, "red"));
        }
        if(greenRangeFormula == null)
        {
            if(rangeFormula != null)
            {
                postCurve.setGreen(evaluateAll(input, rangeFormula, "green"));
            }
        }
        else
        {
            postCurve.setGreen(evaluateAll(input,greenRangeFormula, "green"));
        }

        if(blueRangeFormula == null)
        {
            if(rangeFormula != null)
            {
                postCurve.setRed(evaluateAll(input, rangeFormula,"blue"));
            }
        }
        else
        {
            postCurve.setBlue(evaluateAll(input,blueRangeFormula,"blue"));
        }

        input.setPixel(postCurve);


    }

    //getters for range starts, ends, and formulas.
    public int getRangeStart()
    {
        return rangeStart;
    }
    public int getRangeEnd()
    {
        return rangeEnd;
    }
    public String getRangeFormula()
    {
        return rangeFormula;
    }

    /**
     * these getters take a String input, and return the correct data for the color given by the string.
     *They take one of "red", "green", or "blue". Other values return the default data.
     */
    public int getRangeStart(String color)
    {
        switch(color)
        {
            case "red": return redRangeStart;
            case "green": return greenRangeStart;
            case "blue": return blueRangeStart;
            default: return rangeStart;
        }
    }
    public int getRangeEnd(String color)
    {
        switch(color)
        {
            case "red": return redRangeEnd;
            case "green": return greenRangeEnd;
            case "blue": return blueRangeEnd;
            default: return rangeEnd;
        }
    }
    public String getRangeFormula(String color)
    {
        switch(color)
        {
            case "red": return redRangeFormula;
            case "green": return greenRangeFormula;
            case "blue": return blueRangeFormula;
            default: return rangeFormula;
        }
    }






}
