package com.demo.dao;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Set;



public class ClassLoaderUtils {
//	private static final Logger logger = LoggerFactory.getLogger(ClassLoaderUtils.class); 
	
    //~ Methods ////////////////////////////////////////////////////////////////

    /**
     * Load all resources with a given name, potentially aggregating all results 
     * from the searched classloaders.  If no results are found, the resource name
     * is prepended by '/' and tried again.
     *
     * This method will try to load the resources using the following methods (in order):
     * <ul>
     *  <li>From Thread.currentThread().getContextClassLoader()
     *  <li>From ClassLoaderUtil.class.getClassLoader()
     *  <li>callingClass.getClassLoader()
     * </ul>
     *
     * @param resourceName The name of the resources to load
     * @param callingClass The Class object of the calling object
     */
	public static Iterator<URL> getResources(String resourceName, Class<?> callingClass, boolean aggregate) throws IOException {

         AggregateIterator<URL> iterator = new AggregateIterator<URL>();
         iterator.addEnumeration(Thread.currentThread().getContextClassLoader().getResources(resourceName));
         if (!iterator.hasNext() || aggregate) {
             iterator.addEnumeration(ClassLoaderUtils.class.getClassLoader().getResources(resourceName));
         }
         if (!iterator.hasNext() || aggregate) {
             ClassLoader cl = callingClass.getClassLoader();
             if (cl != null) {
                 iterator.addEnumeration(cl.getResources(resourceName));
             }
         }
         if (!iterator.hasNext() && (resourceName != null) && ((resourceName.length() == 0) || (resourceName.charAt(0) != '/'))) {
             return getResources('/' + resourceName, callingClass, aggregate);
         }
         return iterator;
     }

     private static URL findResourceFromClassLoader(String resourceName, Class<?> callingClass){
    	 if(resourceName.charAt(0)=='/'){
    		 resourceName = resourceName.substring(1);
    	 }
    	 URL url = callingClass.getClassLoader().getResource(resourceName);
    	 if(url==null){
    		 String newResourceName = "META-INF/";
    		 newResourceName = newResourceName.concat(resourceName);
    		 url = callingClass.getClassLoader().getResource(newResourceName);
    	 }
    	 return url;
     }
     private static URL getResourceForDepth(String resourceName, Class<?> callingClass){
     	URL url = callingClass.getResource(resourceName);
     	if(url==null && resourceName.charAt(0)!='/'){
     		String pkgName = callingClass.getPackage().getName().replace('.', '/');
     		String newResourceName = "/".concat(pkgName);
     		int firstIndex = newResourceName.indexOf('/');
     		int lastIndex = newResourceName.lastIndexOf('/');
     		while(lastIndex>firstIndex){
     			newResourceName = newResourceName.substring(0, lastIndex);
     			url = callingClass.getResource(newResourceName.concat("/").concat(resourceName));
     			if(url!=null){
     				break;
     			}
     			lastIndex = newResourceName.lastIndexOf('/');
     		}
     	}
     	if(url==null){
     		url = findResourceFromClassLoader(resourceName, callingClass);
     	}
     	return url;
     }
     private static URL getResourceForBreadth(String resourceName, Class<?> callingClass){
     	URL url = findResourceFromClassLoader(resourceName, callingClass);
     	if(url==null && resourceName.charAt(0)!='/'){
     		String pkgName = callingClass.getPackage().getName();
         	String [] tmp = pkgName.split("\\.");
         	String packName = "/";
         	for(int i=0; i<tmp.length; i++){
         		packName = packName.concat(tmp[i]).concat("/");
         		url = callingClass.getResource(packName.concat(resourceName));
         		if(url!=null){
         			break;
         		}
         	}
     	}
     	return url;
     }
    /**
    * Load a given resource. default: 广度加载(比外到内)
    *
    *
    * This method will try to load the resource using the following methods (in order):
    * <ul>
    *  <li>From Thread.currentThread().getContextClassLoader()
    *  <li>From ClassLoaderUtil.class.getClassLoader()
    *  <li>callingClass.getClassLoader()
    * </ul>
    *
    * @param resourceName The name IllegalStateException("Unable to call ")of the resource to load
    * @param callingClass The Class object of the calling object
    * 
    */
	public static URL getResource(String resourceName, Class<?> callingClass) {
       return getResource(resourceName, callingClass, true);
    }

    public static URL getResource(String resourceName, Class<?> callingClass, boolean breadth){
    	if(breadth){
    		return getResourceForBreadth(resourceName, callingClass);
    	}
    	return getResourceForDepth(resourceName, callingClass);
    }

    /**
    * This is a convenience method to load a resource as a stream.
    *
    * The algorithm used to find the resource is given in getResource()
    *
    * @param resourceName The name of the resource to load
    * @param callingClass The Class object of the calling object
    */
	public static InputStream getResourceAsStream(String resourceName, Class<?> callingClass) {
        URL url = getResource(resourceName, callingClass);

        try {
            return (url != null) ? url.openStream() : null;
        } catch (IOException e) {
            return null;
        }
    }

    /**
    * Load a class with a given name.
    *
    * It will try to load the class in the following order:
    * <ul>
    *  <li>From Thread.currentThread().getContextClassLoader()
    *  <li>Using the basic Class.forName()
    *  <li>From ClassLoaderUtil.class.getClassLoader()
    *  <li>From the callingClass.getClassLoader()
    * </ul>
    *
    * @param className The name of the class to load
    * @param callingClass The Class object of the calling object
    * @throws ClassNotFoundException If the class cannot be found anywhere.
    */
	public static Class<?> loadClass(String className, Class<?> callingClass) throws ClassNotFoundException {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException ex) {
                try {
                    return ClassLoaderUtils.class.getClassLoader().loadClass(className);
                } catch (ClassNotFoundException exc) {
                    return callingClass.getClassLoader().loadClass(className);
                }
            }
        }
    }

    /**
     * Aggregates Enumeration instances into one iterator and filters out duplicates.  Always keeps one
     * ahead of the enumerator to protect against returning duplicates.
     */
    static class AggregateIterator<E> implements Iterator<E> {

        LinkedList<Enumeration<E>> enums = new LinkedList<Enumeration<E>>();
        Enumeration<E> cur = null;
        E next = null;
        Set<E> loaded = new HashSet<E>();

        public AggregateIterator<E> addEnumeration(Enumeration<E> e) {
            if (e.hasMoreElements()) {
                if (cur == null) {
                    cur = e;
                    next = e.nextElement();
                    loaded.add(next);
                } else {
                    enums.add(e);
                }
            }
            return this;
        }

        public boolean hasNext() {
            return (next != null);
        }

        public E next() {
            if (next != null) {
                E prev = next;
                next = loadNext();
                return prev;
            } else {
                throw new NoSuchElementException();
            }
        }

        private Enumeration<E> determineCurrentEnumeration() {
            if (cur != null && !cur.hasMoreElements()) {
                if (enums.size() > 0) {
                    cur = enums.removeLast();
                } else {
                    cur = null;
                }
            }
            return cur;
        }

        private E loadNext() {
            if (determineCurrentEnumeration() != null) {
                E tmp = cur.nextElement();
                int loadedSize = loaded.size();
                while (loaded.contains(tmp)) {
                    tmp = loadNext();
                    if (tmp == null || loaded.size() > loadedSize) {
                        break;
                    }
                }
                if (tmp != null) {
                    loaded.add(tmp);
                }
                return tmp;
            }
            return null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}