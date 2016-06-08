package de.muenchen.allg.itd51.wollmux.core.parser.scanner;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import de.muenchen.allg.itd51.wollmux.core.parser.PathProcessor;
import de.muenchen.allg.itd51.wollmux.core.util.Logger;

/**
 * Reads a file and included ones and and produces a list of tokens in order
 * with the appearance in the files.
 * 
 * @author Daniel Sikeler daniel.sikeler@muenchen.de
 */
public class Scanner implements Iterator<Token>, Closeable
{

  /** Stack of running tokenizers. */
  private final Deque<Tokenizer> stack = new LinkedList<Tokenizer>();
  /** The file from which this scanner reads. */
  private final URL filename;
  /** Is this the first time to read. */
  private boolean isStart;

  /**
   * Create a new ScannerReader and read the UTF-8 byte ordering mark if there
   * is one.
   * 
   * @param filename
   *          The URL of the file to be read.
   * @throws ScannerException
   *           The ScannerReader can't be initialized, because there is no file
   *           or it can't be read.
   */
  public Scanner(final URL filename) throws ScannerException
  {
    isStart = true;
    try
    {
      this.filename = filename;
      stack.push(new Tokenizer(filename.openStream()));
    } catch (IOException e)
    {
      throw new ScannerException(filename.getPath() + "could not be read.", e);
    }
  }

  /**
   * Create a new ScannerReader and read the UTF-8 byte ordering mark if there
   * is one.
   * 
   * @param stream
   *          The stream to read.
   * @throws ScannerException
   *           The ScannerReader can't be initialized, because there is no file
   *           or it can't be read.
   */
  public Scanner(final InputStream stream) throws ScannerException
  {
    isStart = true;
    try
    {
      this.filename = new URL("file:.");
      stack.push(new Tokenizer(stream));
    } catch (MalformedURLException e)
    {
      throw new ScannerException("Couldn't create URL");
    }
  }

  @Override
  public void close() throws ScannerException
  {
    try
    {
      for (final Tokenizer tokenizer : stack)
      {
        tokenizer.close();
      }
    } catch (final IOException e)
    {
      throw new ScannerException("The file reader can't be closed", e);
    }
  }

  @Override
  public boolean hasNext()
  {
    return stack.isEmpty() ? false : stack.peek().hasNext();
  }

  @Override
  public Token next()
  {
    if (isStart)
    {
      isStart = false;
      return new Token(filename.getFile(), TokenType.NEW_FILE);
    }
    final Token token = stack.peek().next();
    if (token.getType() == TokenType.NEW_FILE)
    {
      try
      {
        stack.push(new Tokenizer(new URL(filename, PathProcessor
            .processInclude(token.getContent())).openStream()));
      } catch (final IOException e)
      {
        Logger.error("Could not open file for token " + token.getContent()
            + ".", e);
        throw new NoSuchElementException("No more tokens avaiable");
      }
    } else if (token.getType() == TokenType.END_FILE)
    {
      stack.pop();
    }
    return token;
  }

  /** This method isn't supported. */
  @Override
  public void remove()
  {
    throw new UnsupportedOperationException("A token can't be removed");
  }

}
