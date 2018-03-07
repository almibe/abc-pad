function main(sources) {
  const saveClick$ = domSource.select('#save').events('click')
  const showLoadClick$ = domSource.select('#showLoad').events('click')
  const loadClick$ = domSource.select('#load').events('click')
  const nameChange$ = domSource.select('#name').events('change')
  const idChange$ = domSource.select('#id').events('change')
  const documentChange$ = domSource.select('#abcEditor').events('change')

  const nameValue$ = nameChange$.map(e => e.target.value).startWith("")
  const idValue$ = idChange$.map(e => e.target.value).startWith(null)
  const documentValue$ = documentChange$.map(e => e.target.value).startWith("")

  const state$ = xs.combine(idValue$, nameValue$, documentValue$)
    .map(([id, name, document]) => {
      return { id, name, document }
    }).startWith({ id: null, name: "", document: "" })

  const abcEditor = ABCEditor({ DOM: sources.DOM, state: state$ })
  const header = Header({ DOM: sources.DOM, state: state$ })
  const loadDialog = LoadDialog({ DOM: sources.DOM, state: state$ })

  const saveRequest$ = saveClick$.compose(sampleCombine(state$)).map(function([event, state]) {
    return {
      url: 'documents',
      method: 'POST',
      category: 'save-document',
      send: { id: state.id, name: state.name, document: state.document }
    }
  })

  const saveResponse$ = httpSource.select('save-document').flatten() //TODO finish

  const fetchAllRequest$ = dialogVisible$.filter(i => i).map((input) =>
    {
      url: 'documents',
      category: 'fetch-all'
    }
  )

  const fetchAllResponse$ = httpSource.select('fetch-all').flatten() //TODO finish

  const loadRequest$ = loadClick$.compose(sampleCombine(state$)).map(function([event, state]) {
    return {
      url: 'documents',
      category: 'load-document',
      query: { id: state.idToLoad } //TODO state.idToLoad doesn't exist yet
    }
  })
}
