public final class ContactSearchResult {
  public final String name;
  public final String contactType;
  public final String contact;

  public ContactSearchResult(String name, String contactType, String contact) {
    this.name = name;
    this.contactType = contactType;
    this.contact = contact;
  }

  @Override
  public String toString() {
    return String.format("%16s's %-8s : %s", name, contactType, contact);
  }
}
