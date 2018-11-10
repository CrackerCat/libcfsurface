package eu.chainfire.libcfsurface.gl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

public class GLTextRendererBase implements GLObject {
    public static enum Justification { LEFT, CENTER, RIGHT };
    public static final int WIDTH_AUTO = 0;

    protected final GLTextureManager mTextureManager;
    protected final GLHelper mHelper;
    protected final Paint mPaint;
    protected final int mTextSize;
    protected final int mLineHeight;
    protected final int mVerticalPadding;
    protected final int mHorizontalPadding;

    protected GLTextRendererBase(GLTextureManager textureManager, GLHelper helper, Typeface typeface, int lineHeight) {
        mTextureManager = textureManager;
        mHelper = helper;
        mLineHeight = lineHeight;
        mTextSize = (int)Math.round(Math.ceil((float)mLineHeight / 1.2f));
        mVerticalPadding = (mLineHeight - mTextSize) / 2;
        mHorizontalPadding = mLineHeight / 4;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTypeface(typeface);
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(Color.WHITE);
        mPaint.setShadowLayer(1.0f, 1.0f, 1.0f, Color.BLACK);
    }

    protected Bitmap getBitmap(String text, int color, int width, Justification justification, Bitmap inBitmap) {
        Rect r = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), r);
        mPaint.setColor(color);
        int textWidth = (r.right - r.left) + (mHorizontalPadding * 2);

        if (width == WIDTH_AUTO) width = textWidth;

        Bitmap bitmap;
        if (inBitmap == null) {
            bitmap = Bitmap.createBitmap(width, mLineHeight, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = inBitmap;
            width = bitmap.getWidth();
            bitmap.eraseColor(0x00000000);
        }

        int leftMargin = 0;
        switch (justification) {
            case LEFT: break;
            case CENTER: leftMargin = (width - textWidth) / 2; break;
            case RIGHT: leftMargin = (width - textWidth);
        }

        Canvas c = new Canvas(bitmap);
        c.drawText(text, -r.left + mHorizontalPadding + leftMargin, -r.top + mVerticalPadding, mPaint);
        return bitmap;
    }

    protected GLPicture getPicture(Bitmap bitmap) {
        return new GLPicture(mTextureManager, bitmap);
    }

    protected GLPicture getPicture(String text, int color, int width, Justification justification, Bitmap inBitmap) {
        return getPicture(getBitmap(text, color, width, justification, inBitmap));
    }

    @Override
    public void destroy() {
    }

    public int getLineHeight() {
        return mLineHeight;
    }
}