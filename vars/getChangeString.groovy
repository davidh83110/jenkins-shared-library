def call(changeSets) {
  MAX_MSG_LEN = 200
  def changeString = ""

  echo "Gathering Git changes"
  def changeLogSets = changeSets
  for (int i = 0; i < changeLogSets.size(); i++) {
    def entries = changeLogSets[i].items
    for (int j = 0; j < entries.length; j++) {
      def entry = entries[j]
      truncated_msg = entry.msg.take(MAX_MSG_LEN)
      changeString += "- ${truncated_msg} / [${entry.author}]\n"
    }
  }

  if (!changeString) {
    changeString = " - No new changes"
  }
  echo changeString
  return changeString
}