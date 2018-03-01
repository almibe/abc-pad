import xs from 'xstream'
import { html } from 'snabbdom-jsx'

export function LoadButton(sources) {
  const domSource = sources.DOM
  const state$ = sources.state

  const vdom$ = state$.map( state =>

  )
  return { DOM: vdom$ }
}
