package tasks;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Resource implements Serializable {
	
    
	private static final long serialVersionUID = 1L;
	private String path;
    private String name;

    public Resource(File file){
        this.path = file.getAbsolutePath();
        this.name = file.getName();
    }

    public BufferedImage getImage() {
        try{
            return ImageIO.read(new File(path));
        } catch (IOException exc){
            exc.printStackTrace();
            return null;
        }
    }

    @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Resource other = (Resource) obj;
		return Objects.equals(path, other.path);
	}

    @Override
	public int hashCode() {
		return Objects.hash(path);
	}

    @Override
    public String toString() { 
    	return path;
    }

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
    
}
