package com.binbin.testas.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLUtils;

import com.binbin.testas.MainActivity;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Screen {
    private Context mContext;
    private float ratio;

    // Our vertices.
    private float vertices[];

    // The order we like to connect them.
    private short[] indices = { 0, 1, 2, 0, 2, 3 };//封闭的三角形

    // Our vertex buffer.
    private FloatBuffer vertexBuffer;

    // Our index buffer.
    private ShortBuffer indexBuffer;
    // Our UV texture buffer.
    private FloatBuffer mTextureBuffer; // New variable.

    float textureCoordinates[] ;
    // Our texture id.
    private int mTextureId = -1; // New variable.

    public Screen(Context mContext) {
        this.mContext=mContext;
        ratio= (float) MainActivity.sHeight/MainActivity.sWidth;
        //把UV坐标中的（0，1）映射到顶点0，（1，1）映射到顶点2等等
        textureCoordinates=new float[]{
                0f, 0f,
                0f, 1f,
                1f, 1f,
                1f, 0f,
        };
        ByteBuffer byteBuf = ByteBuffer .allocateDirect(textureCoordinates.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mTextureBuffer = byteBuf.asFloatBuffer();
        mTextureBuffer.put(textureCoordinates);
        mTextureBuffer.position(0);

        vertices= new float[]{
            -1.0f,  ratio, 0.0f,  // 0, Top Left
                    -1.0f, -ratio, 0.0f,  // 1, Bottom Left
                    1.0f, -ratio, 0.0f,  // 2, Bottom Right
                    1.0f,  ratio, 0.0f,  // 3, Top Right
        };

        // a float is 4 bytes, therefore we
        // multiply the number if
        // vertices with 4.
        ByteBuffer vbb
                = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        // short is 2 bytes, therefore we multiply
        //the number if
        // vertices with 2.
        ByteBuffer ibb
                = ByteBuffer.allocateDirect(indices.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        indexBuffer = ibb.asShortBuffer();
        indexBuffer.put(indices);
        indexBuffer.position(0);

    }

    /**
     * This function draws our square on screen.
     * @param gl
     */
    public void draw(GL10 gl,Bitmap bit) {
        loadGLTexture(gl,bit);
        // Counter-clockwise winding.
        gl.glFrontFace(GL10.GL_CCW);
        // Enable face culling.
        gl.glEnable(GL10.GL_CULL_FACE);
        // What faces to remove with the face culling.
        gl.glCullFace(GL10.GL_BACK);

        // Enabled the vertices buffer for writing
        //and to be used during
        // rendering.
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        // Specifies the location and data format of
        //an array of vertex
        // coordinates to use when rendering.
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0,
                vertexBuffer);

        // Telling OpenGL to enable textures.
        gl.glEnable(GL10.GL_TEXTURE_2D);
        // Tell OpenGL where our texture is located.
        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId);
        // Tell OpenGL to enable the use of UV coordinates.
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        // Telling OpenGL where our UV coordinates are.
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);

        gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
                GL10.GL_UNSIGNED_SHORT, indexBuffer);

        // Disable the vertices buffer.
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        // Disable face culling.
        gl.glDisable(GL10.GL_CULL_FACE);
        // Disable the use of UV coordinates.
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        // Disable the use of textures.
        gl.glDisable(GL10.GL_TEXTURE_2D);
        bit.recycle();
    }

    /**
     * Loads the texture.
     *
     * @param gl
     */
    public void loadGLTexture(GL10 gl,Bitmap bit) { // New function
        // Generate one texture pointer...
        int[] textures = new int[1];
        gl.glGenTextures(1, textures, 0);
        mTextureId = textures[0];

        // ...and bind it to our array
        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId);

        // Create Nearest Filtered Texture
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
                GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
                GL10.GL_NEAREST);

        // Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
                GL10.GL_CLAMP_TO_EDGE);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
                GL10.GL_CLAMP_TO_EDGE);

        // Use the Android GLUtils to specify a two-dimensional texture image
        // from our bitmap
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bit, 0);
    }
}
