import { remote } from 'electron';

export function newDoc() {
  if (this.saveCheck()) {
    this.editorContent = abcTemplate;
    this.$nextTick(() => { this.abcEditor.fireChanged(); })
    this.$refs.abcEditor.focus();
  }
}

export function saveDoc() {
  if (this.fileReference != null) {
    this.handleSaveAs();
  } else {
    this.writeFile();
  }
}

export function saveDocAs() {
  this.handleSaveAs()
}

export function loadDoc() {
  if (this.saveCheck()) {
    this.handleLoad()
  }
}

function saveCheck: () {
  if (this.editorContent === abcTemplate && this.fileReference === null) {
    return true;
  } else {
    if (this.fileReference === null || this.checkUnsavedFile()) {
      const result = this.promptSave()
      if (result === 0) { //yes
        return this.handleSave();
      } else if (result === 1) { //no
        return true;
      } else { //cancel
        return false;
      }
    } else {
      return true;
    }
  }
}

function checkUnsavedFile() {
  //TODO
}

function promptSave() {
  //TODO prompt user to save with dialog Do you have to save?  yes/no/cancel
  return remote.dialog.showMessageBoxSync({
    //TODO
  });
}

function handleSaveAs() {
  //TODO show show as dialog
  //TODO update file reference
  this.writeFile();
}

function handleLoad() {
  //TODO show load dialog
  //TODO set file reference
  //TODO load file
},

function writeFile() {
  //TODO
}
