import type { 
  SchuelerListeEintrag,
  LehrerListeEintrag,
  KlassenListeEintrag,
  KursListeEintrag,
  JahrgangsListeEintrag,
  FaecherListeEintrag,
  ReligionEintrag,
  BenutzerListeEintrag,
  BenutzergruppeListeEintrag
} from '@svws-nrw/svws-core-ts'

import type { Lehrer } from '../apps/lehrer/Lehrer'
import type { Schueler } from '../apps/schueler/schueler'
import type { Kurse } from '../apps/kurse/Kurse'
import type { Klassen } from '../apps/klassen/Klassen'
import type { Faecher } from '../apps/faecher/Faecher'
import type { Jahrgaenge } from '../apps/jahrgaenge/Jahrgaenge'
import type { Religionen } from '../apps/kataloge/religionen/Religionen'
import { Benutzer } from '~/apps/schule/benutzerverwaltung/Benutzer'
import { Benutzergruppe } from '~/apps/schule/benutzerverwaltung/Benutzergruppe'

// Das sind nur die Routen, die auch dynamische IDs haben können
export type RouteAppTypesMap = {
  schueler: Schueler
  lehrer: Lehrer
  klassen: Klassen
  kurse: Kurse
  jahrgaenge: Jahrgaenge
  faecher: Faecher
  religionen: Religionen
  benutzer: Benutzer
  benutzergruppe: Benutzergruppe
}

export type RouteItemTypesMap = {
  schueler: SchuelerListeEintrag
  lehrer: LehrerListeEintrag
  klassen: KlassenListeEintrag
  kurse: KursListeEintrag
  jahrgaenge: JahrgangsListeEintrag
  faecher: FaecherListeEintrag
  religionen: ReligionEintrag
  benutzer: BenutzerListeEintrag
  benutzergruppe: BenutzergruppeListeEintrag
}

export type RouteNames = keyof RouteItemTypesMap

export type RouteItems = RouteItemTypesMap[keyof RouteItemTypesMap]
export type RouteApps = RouteAppTypesMap[keyof RouteAppTypesMap]
