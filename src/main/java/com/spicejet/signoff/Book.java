package com.spicejet.signoff;

public class Book {

    private String noFirst;
    private String typeFirst;
    private String revisionFirst;
    private String byFirst;
    private String noSecond;
    private String typeSecond;
    private String revisionSecond;
    private String bySecond;


    public static Book getCopiedBookInstance(Book book) {
        Book value = new Book();
        value.setNoFirst(book.getNoFirst());
        value.setTypeFirst(book.getTypeFirst());
        value.setRevisionFirst(book.getRevisionFirst());
        value.setByFirst(book.getByFirst());
        value.setNoSecond(book.getNoSecond());
        value.setTypeSecond(book.getTypeSecond());
        value.setRevisionSecond(book.getRevisionSecond());
        value.setBySecond(book.getBySecond());
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return getNoFirst().equals(book.getNoFirst()) && getTypeFirst().equals(book.getTypeFirst()) &&
                getRevisionFirst().equals(book.getRevisionFirst()) && getByFirst().equals(book.getByFirst()) &&
                getNoSecond().equals(book.getNoSecond()) && getTypeSecond().equals(book.getTypeSecond()) &&
                getRevisionSecond().equals(book.getRevisionSecond()) && getBySecond().equals(book.getBySecond());
    }

    @Override
    public int hashCode() {
        int result = getNoFirst().hashCode();
        result = 31 * result + getTypeFirst().hashCode();
        result = 31 * result + getRevisionFirst().hashCode();
        result = 31 * result + getByFirst().hashCode();
        result = 31 * result + getNoSecond().hashCode();
        result = 31 * result + getTypeSecond().hashCode();
        result = 31 * result + getRevisionSecond().hashCode();
        result = 31 * result + getBySecond().hashCode();
        return result;
    }

    public String getNoFirst() {
        return noFirst;
    }

    public void setNoFirst(String noFirst) {
        this.noFirst = noFirst;
    }

    public String getTypeFirst() {
        return typeFirst;
    }

    public void setTypeFirst(String typeFirst) {
        this.typeFirst = typeFirst;
    }

    public String getRevisionFirst() {
        return revisionFirst;
    }

    public void setRevisionFirst(String revisionFirst) {
        this.revisionFirst = revisionFirst;
    }

    public String getByFirst() {
        return byFirst;
    }

    public void setByFirst(String byFirst) {
        this.byFirst = byFirst;
    }

    public String getNoSecond() {
        return noSecond;
    }

    public void setNoSecond(String noSecond) {
        this.noSecond = noSecond;
    }

    public String getTypeSecond() {
        return typeSecond;
    }

    public void setTypeSecond(String typeSecond) {
        this.typeSecond = typeSecond;
    }

    public String getRevisionSecond() {
        return revisionSecond;
    }

    public void setRevisionSecond(String revisionSecond) {
        this.revisionSecond = revisionSecond;
    }

    public String getBySecond() {
        return bySecond;
    }

    public void setBySecond(String bySecond) {
        this.bySecond = bySecond;
    }
}
