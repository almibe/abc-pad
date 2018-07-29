import { h } from 'hyperapp'

export const LoadDialog = ({dialogState, hideLoad, documentList, loadDocument}) =>
  <div class={`modal ${dialogState}`}>
    <div class="modal-background" onclick={() => hideLoad()}></div>
    <div class="modal-card">
      <header class="modal-card-head">
        <p class="modal-card-title">Manage Documents</p>
        <button class="delete" aria-label="close" onclick={() => hideLoad()}></button>
      </header>
      <section class="modal-card-body">
        {documentList.length < 1 &&
          <p>No documents</p>
        }
        {documentList.length > 0 &&
          <p>Documents</p>
        }
        <ul>
        {documentList.map(({ id, title, composer }) => (
          <li> <a onclick={() => loadDocument(id)}>{composer} - {title}</a> </li>
        ))}
        </ul>
      </section>
    </div>
  </div>

    showLoad: (value) => (state, actions) => {
      controllers.loadDocumentList(actions)
      return {dialogState: "is-active"}
    },
    hideLoad: function(state) {
      return {dialogState: ""}
    },
