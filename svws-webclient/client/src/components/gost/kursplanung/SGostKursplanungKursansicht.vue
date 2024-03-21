<template>
	<div v-if="!blockungstabelleVisible" />
	<div v-else class="content-card mr-16 h-full">
		<div class="h-full flex flex-col">
			<svws-ui-table :items="GostKursart.values()" :columns="cols" disable-footer scroll has-background :style="!blockungstabelleVisible ? 'margin-left: 0; margin-right: 0; opacity: 0;' : ''" class="pr-4">
				<template #header>
					<div role="row" class="svws-ui-tr select-none">
						<div role="columnheader" class="svws-ui-td svws-divider" :class="allowRegeln ? 'col-span-7' : 'col-span-6'">
							<div class="flex items-center justify-between w-full -my-2">
								<div class="flex flex-row gap-2">
									<span>Schiene</span>
								</div>
								<div class="flex flex-row gap-2">
									<svws-ui-button type="transparent" size="small" @click="setZeigeSchienenbezeichnungen(!zeigeSchienenbezeichnungen())" :title="zeigeSchienenbezeichnungen() ? 'Zeige nur die Schienenummer':'Zeige den vollständigen Schienennamen'">
										<span v-if="zeigeSchienenbezeichnungen()" class="icon i-ri-text" />
										<span v-else class="icon i-ri-expand-height-line" />
									</svws-ui-button>
									<template v-if="allowRegeln">
										<svws-ui-button type="icon" size="small" @click="add_schiene" title="Schiene hinzufügen">
											<span class="icon i-ri-add-line" />
										</svws-ui-button>
									</template>
								</div>
							</div>
						</div>
						<div role="columnheader" class="svws-ui-td svws-align-center !overflow-visible !px-0" v-for="(s, index) in schienen" :key="s.id" :class="{'svws-divider': index + 1 < schienen.size()}">
							<div class="flex justify-center text-center items-center w-full relative">
								<svws-ui-tooltip v-if="s.id === edit_schienenname" keep-open>
									<span class="opacity-50 border-b border-transparent">{{ s.nummer }}</span>
									<template #content>
										<div class="py-2">
											<svws-ui-text-input :model-value="s.bezeichnung" focus headless
												@change="name => patch_schiene(s, name)"
												@blur="name => patch_schiene(s, name)"
												@keyup.escape="edit_schienenname=undefined" class="text-center" />
										</div>
									</template>
								</svws-ui-tooltip>
								<template v-else-if="zeigeSchienenbezeichnungen()">
									<div>
										<span style="writing-mode: vertical-lr;" :class="{ 'border-l border-dotted hover:border-transparent': allowRegeln }" class="cursor-text rotate-180 normal-nums min-h-[1.5ch] w-full inline-flex justify-center" :title="'Namen bearbeiten (' + s.bezeichnung + ')'" @click="allowRegeln && (edit_schienenname = s.id)">
											{{ s.bezeichnung }}
										</span>
										<span v-if="allow_del_schiene(s)" @click="del_schiene(s)" class="icon-sm inline-block i-ri-delete-bin-line cursor-pointer absolute top-1/2 transform -translate-y-1/2 right-px opacity-50 hover:opacity-100 hover:icon-error" />
									</div>
								</template>
								<template v-else>
									<span :class="{ 'border-b border-dotted hover:border-transparent': allowRegeln }" class="cursor-text normal-nums min-w-[1.5ch] h-full inline-flex items-center justify-center" :title="'Namen bearbeiten (' + s.bezeichnung + ')'" @click="allowRegeln && (edit_schienenname = s.id)">{{ s.nummer }}</span>
									<span v-if="allow_del_schiene(s)" @click="del_schiene(s)" class="icon-sm inline-block i-ri-delete-bin-line cursor-pointer absolute top-1/2 transform -translate-y-1/2 right-px opacity-50 hover:opacity-100 hover:icon-error" />
								</template>
							</div>
						</div>
					</div>
					<div role="row" class="svws-ui-tr select-none">
						<div role="columnheader" class="svws-ui-td svws-divider" :class="allowRegeln ? 'col-span-7' : 'col-span-6'">
							Schülerzahl
						</div>
						<div role="columnheader" class="svws-ui-td svws-align-center !px-0" v-for="(s, index) in schienen" :key="s.id" :class="{'svws-divider': index + 1 < schienen.size()}">
							<template v-if="getAnzahlSchuelerSchiene(s.id) > 0">
								<span class="inline-flex items-center gap-1">{{ getAnzahlSchuelerSchiene(s.id) }}</span>
							</template>
							<span v-else class="opacity-25">0</span>
						</div>
					</div>
					<div role="row" class="svws-ui-tr select-none">
						<div role="columnheader" class="svws-ui-td svws-divider" :class="allowRegeln ? 'col-span-7' : 'col-span-6'">
							Kollisionen
						</div>
						<div role="columnheader" class="svws-ui-td svws-align-center !px-0" v-for="(s, index) in schienen" :key="s.id" :class="{'text-error': getAnzahlKollisionenSchiene(s.id) > 0, 'svws-divider': index + 1 < schienen.size()}">
							<svws-ui-tooltip v-if="getAnzahlKollisionenSchiene(s.id) > 0" autosize>
								<span class="inline-flex items-center"><span class="icon-sm icon-error i-ri-alert-line" />{{ getAnzahlKollisionenSchiene(s.id) }}</span>
								<template #content>
									<template v-for="list, indexTooltip of getErgebnismanager().getOfSchieneTooltipKurskollisionenAsData(s.id)" :key="indexTooltip">
										<p>
											<template v-for="pair, listIndex of list" :key="pair.a.id">
												<span v-if="listIndex === 0" class="font-bold">{{ getDatenmanager().kursGetName(pair.a.id) }}:&nbsp;</span>
												<span v-else>{{ getDatenmanager().kursGetName(pair.a.id) }} ({{ pair.b ?? 0 }}){{ listIndex !== list.size()-1 ? ',&nbsp;': '' }}</span>
											</template>
										</p>
									</template>
								</template>
							</svws-ui-tooltip>
							<span v-else class="opacity-25 font-normal">0</span>
						</div>
					</div>
					<div role="row" class="svws-ui-tr select-none">
						<div role="columnheader" class="svws-ui-td svws-align-center" aria-label="Alle auswählen">
							<svws-ui-checkbox :model-value="getDatenmanager().kursGetAnzahl() === getKursauswahl().size()" :indeterminate="(getKursauswahl().size() > 0) && (getKursauswahl().size() < getDatenmanager().kursGetAnzahl())"
								@update:model-value="updateKursauswahl" headless />
						</div>
						<div role="columnheader" class="svws-ui-td svws-sortable-column" @click="kurssortierung.value = (kurssortierung.value === 'kursart') ? 'fach' : 'kursart'" :class="{'col-span-2': allowRegeln, 'col-span-1': !allowRegeln}">
							<span>Kurs</span>
							<span class="svws-sorting-icon">
								<span class="icon-sm i-ri-arrow-up-down-line" :class="{'opacity-75': kurssortierung.value === 'kursart'}" />
							</span>
						</div>
						<div role="columnheader" class="svws-ui-td">Lehrkraft</div>
						<div class="svws-ui-td svws-align-center" title="Kooperation">Koop</div>
						<div class="svws-ui-td svws-align-center" title="Fachwahlen">
							<svws-ui-tooltip>
								FW
								<template #content>
									Fachwahlen
								</template>
							</svws-ui-tooltip>
						</div>
						<div class="svws-ui-td svws-align-center svws-divider" title="Differenz">Diff</div>
						<!--Schienen-->
						<template v-if="allowRegeln">
							<template v-for="(schiene, index) in schienen" :key="schiene.id">
								<div @dragover="if (istDropZoneSchiene(schiene)) $event.preventDefault();" @drop="openModalRegelKursartSchiene(schiene)" class="svws-ui-td svws-align-center text-black/25 dark:text-white/25 !p-0 hover:text-black dark:hover:text-white relative group" role="columnheader"
									:class="{ 'bg-primary/5 text-primary hover:text-primary dark:text-primary dark:hover:text-primary': istDropZoneSchiene(schiene), 'svws-divider': index + 1 < schienen.size() }">
									<div :key="schiene.id" @click="openModalRegelKursartSchiene(schiene)" class="select-none text-center" :class="dragSperreSchiene !== undefined ? ['cursor-grabbing'] : ['cursor-grab']" :draggable="true" @dragstart="dragSchieneStarted(schiene)" @dragend="dragSchieneEnded">
										<span class="rounded-sm w-3 absolute top-1 left-1 max-w-[0.75rem]">
											<span class="icon-sm inline-block i-ri-draggable -ml-0.5 -my-2 opacity-25 group-hover:opacity-100" />
										</span>
										<span class="icon inline-block i-ri-lock-unlock-line opacity-25 group-hover:opacity-100" />
									</div>
								</div>
							</template>
						</template>
						<template v-else>
							<div v-for="(schiene, index) in schienen" :key="schiene.id" role="columnheader" class="svws-ui-td !px-0 svws-align-center" :class="{'svws-divider': index + 1 < schienen.size() }">
								<span class="icon inline-block i-ri-lock-unlock-line opacity-10" />
							</div>
						</template>
					</div>
				</template>

				<template #body>
					<template v-for="fachwahl in fachwahlListe" :key="fachwahl">
						<template v-if="istFachwahlVorhanden(fachwahl.fachwahlen, fachwahl.kursart)">
							<template v-if="listeDerKurse(fachwahl).isEmpty() && getAnzahlFachwahlen(fachwahl) !== 0 && allowRegeln">
								<div role="row" class="svws-ui-tr svws-disabled-soft select-none" :style="{ '--background-color': bgColor(fachwahl) }" :key="fachwahl.kursart.id">
									<div role="cell" class="svws-ui-td" />
									<div role="cell" class="svws-ui-td" />
									<div role="cell" class="svws-ui-td text-black/50">{{ fachwahl.fachwahlen.kuerzel }}-{{ fachwahl.kursart.kuerzel }}</div>
									<div role="cell" class="svws-ui-td" />
									<div role="cell" class="svws-ui-td" />
									<div role="cell" class="svws-ui-td svws-align-center text-black/50" @click="toggleSchuelerFilterFachwahl(fachwahl)">
										{{ getAnzahlFachwahlen(fachwahl) }}
									</div>
									<div role="cell" class="svws-ui-td" />
									<div role="cell" class="svws-ui-td svws-align-center" :style="{'gridColumn': 'span ' + getDatenmanager().schieneGetListe().size()}">
										<svws-ui-button type="transparent" @click="add_kurs(fachwahl)" title="Kurs anlegen">
											<span class="inline-flex items-center text-button -mr-0.5">
												<span class="icon i-ri-book-2-line" />
												<span class="icon-sm i-ri-add-line -ml-0.5 text-sm" />
											</span>
											Kurs anlegen
										</svws-ui-button>
									</div>
								</div>
							</template>
							<template v-else>
								<template v-for="kurs in listeDerKurse(fachwahl)" :key="kurs.id">
									<div role="row" class="svws-ui-tr select-none" :style="{ '--background-color': bgColor(fachwahl) }" :class="{'font-bold': (schuelerFilter().fach === kurs.fach_id) && ((schuelerFilter().kursart?.id === kurs.kursart) || (schuelerFilter().kursart === undefined)), 'svws-expanded': kursdetailAnzeigen === kurs.id}">
										<div role="cell" class="svws-ui-td svws-align-center cursor-pointer">
											<svws-ui-checkbox :model-value="getKursauswahl().contains(kurs.id)" @update:model-value="getKursauswahl().contains(kurs.id) ? getKursauswahl().remove(kurs.id) : getKursauswahl().add(kurs.id)" headless />
										</div>
										<template v-if="allowRegeln">
											<div role="cell" class="svws-ui-td svws-align-center cursor-pointer p-0 items-center hover:text-black" @click="setKursdetailAnzeigen(kurs.id)"
												:class="{'text-black/50' : kursdetailAnzeigen !== kurs.id}"
												title="Kursdetails anzeigen">
												<span v-if="kursdetailAnzeigen === kurs.id" class="icon i-ri-arrow-down-s-line relative" />
												<span v-else class="icon i-ri-arrow-right-s-line relative" />
											</div>
										</template>
										<div role="cell" class="svws-ui-td py-0">
											<div class="flex flex-grow items-center -my-auto h-full">
												<template v-if="kurs.id === editKursID">
													<span class="flex-shrink-0 -my-0.5">{{ getDatenmanager().kursGetNameOhneSuffix(kurs.id) }}<span class="opacity-50">–</span></span>
													<svws-ui-text-input :model-value="kurs.suffix" @blur="suffix => editKursOnBlur(suffix, kurs.id)" @keyup.enter="(e:any)=>e.target.blur()" focus headless class="-my-1" />
												</template>
												<template v-else>
													<span class="underline decoration-dotted decoration-black/50 hover:no-underline underline-offset-2 cursor-text" @click="editKursID=kurs.id">
														{{ props.getDatenmanager().kursGetName(kurs.id) }}
													</span>
												</template>
											</div>
										</div>
										<div role="cell" class="svws-ui-td">
											<template v-if="allowRegeln">
												<svws-ui-select v-model="kurslehrer(kurs).value" autocomplete :item-filter="lehrer_filter" removable headless
													:items="kurslehrer_liste(kurs).value" :item-text="l=> l.kuerzel" title="Lehrkraft" />
											</template>
											<template v-else>
												<span :class="{'opacity-25': !kurslehrer(kurs).value?.kuerzel}">{{ kurslehrer(kurs).value?.kuerzel || '—' }}</span>
											</template>
										</div>
										<div role="cell" class="svws-ui-td svws-align-center svws-no-padding">
											<svws-ui-checkbox headless bw :model-value="kurs.istKoopKurs" @update:model-value="setKoop(kurs, $event)" class="my-auto" />
										</div>
										<template v-if="setze_kursdifferenz(kurs).value && kurs_blockungsergebnis(kurs).value">
											<div role="cell" class="svws-ui-td svws-align-center cursor-pointer group relative" @click="toggle_active_fachwahl(kurs)">
												{{ kursdifferenz(kurs).value[2] }}
												<span class="icon-sm i-ri-filter-fill absolute right-0 top-1" :class="(schuelerFilter().fach === kurs.fach_id) && (schuelerFilter().kursart?.id === kurs.kursart) ? 'text-black' : 'invisible group-hover:visible opacity-25'" />
											</div>
											<div role="cell" class="svws-ui-td svws-align-center svws-divider">
												<span :class="{'opacity-25': kursdifferenz(kurs).value[1] <= 1}">{{ kursdifferenz(kurs).value[1] }}</span>
											</div>
										</template>
										<template v-else>
											<div role="cell" class="svws-ui-td svws-align-center cursor-pointer" @click="toggle_active_fachwahl(kurs)">
												<span class="opacity-25">{{ kursdifferenz(kurs).value[2] }}</span>
											</div>
											<div role="cell" class="svws-ui-td svws-align-center svws-divider">
												<span class="opacity-25">{{ kursdifferenz(kurs).value[1] }}</span>
											</div>
										</template>
										<!-- Es folgen die einzelnen Tabellenzellen für die Schienen der Blockung -->
										<template v-for="(schiene, index) in getErgebnismanager().getMengeAllerSchienen()" :key="schiene.id">
											<!-- Ggf. wird das Element in der Zelle für Drag & Drop dargestellt ... -->
											<div role="cell" class="svws-ui-td svws-align-center !p-[2px]"
												:class="{
													'bg-green-400/50': allowRegeln && (highlightKursAufAnderenKurs(kurs, schiene).value || highlightRechteckDrop(kurs, schiene).value),
													'bg-yellow-400/50': allowRegeln && highlightRechteck(kurs, schiene).value && !highlightKursAufAnderenKurs(kurs, schiene).value,
													'bg-white/50 text-black/25 font-bold': highlightKursVerschieben(kurs).value,
													'svws-disabled': istKursVerbotenInSchiene(kurs, schiene).value,
													'svws-divider': index + 1 < getErgebnismanager().getMengeAllerSchienen().size(),
													'cursor-grabbing': isDragging,
													'cursor-pointer': !isDragging,
												}"
												@dragover.prevent="setDragOver(kurs, schiene)"
												@drop="setDrop(kurs, schiene)">
												<!-- Ist der Kurs der aktuellen Schiene zugeordnet, so ist er draggable, es sei denn, er ist fixiert ... -->
												<div v-if="istZugeordnetKursSchiene(kurs, schiene).value" :draggable="!istKursFixiertInSchiene(kurs, schiene).value"
													@dragstart.stop="setDrag(kurs, schiene)" @dragend="resetDrag" @click="toggleKursAusgewaehlt(kurs)"
													class="select-none w-full h-full rounded-sm flex justify-between items-center group text-black p-px"
													:class="{
														'bg-white text-black font-bold': istKursAusgewaehlt(kurs).value,
														'bg-white/50': !istKursAusgewaehlt(kurs).value,
														'cursor-grab': !isDragging,
													}">
													<span class="icon-sm" :class="istKursFixiertInSchiene(kurs, schiene).value ? ['invisible'] : ['group-hover:bg-white rounded-sm i-ri-draggable -my-0.5 opacity-40 group-hover:opacity-100 inline-block']" />
													<svws-ui-tooltip v-if="getErgebnismanager().getOfKursAnzahlSchuelerExterne(kurs.id) + getErgebnismanager().getOfKursAnzahlSchuelerDummy(kurs.id)">
														{{ getErgebnismanager().getOfKursAnzahlSchueler(kurs.id) + getErgebnismanager().getOfKursAnzahlSchuelerExterne(kurs.id) + props.getErgebnismanager().getOfKursAnzahlSchuelerDummy(kurs.id) }}
														<template #content>{{ getErgebnismanager().getOfKursAnzahlSchueler(kurs.id) }} und {{ getErgebnismanager().getOfKursAnzahlSchuelerExterne(kurs.id) + getErgebnismanager().getOfKursAnzahlSchuelerDummy(kurs.id) }} zusätzliche Kursteilnehmer</template>
													</svws-ui-tooltip>
													<span v-else>{{ getErgebnismanager().getOfKursAnzahlSchueler(kurs.id) }}</span>
													<div class="group" @click.stop="toggleRegelFixiereKursInSchiene(kurs, schiene)">
														<span v-if="istKursFixiertInSchiene(kurs, schiene).value" class="icon-sm i-ri-pushpin-fill inline-block opacity-75 group-hover:opacity-100 -my-0.5" />
														<span v-if="allowRegeln && !istKursFixiertInSchiene(kurs, schiene).value" class="icon-sm i-ri-pushpin-line inline-block opacity-25 group-hover:opacity-100 -my-0.5" />
													</div>
												</div>
												<!-- ... ansonsten ist er nicht draggable -->
												<div v-else class="w-full h-full flex items-center justify-center relative group" @click="toggleRegelSperreKursInSchiene(kurs, schiene)"
													draggable="true" @dragstart.stop="setDrag(kurs, schiene)" @dragend="resetDrag"
													:class="{ 'svws-disabled': istKursVerbotenInSchiene(kurs, schiene).value }">
													<div v-if="highlightKursVerschieben(kurs).value" class="absolute bg-white/50 inset-0 border-2 border-dashed rounded border-black/25" />
													<span v-if="istKursGesperrtInSchiene(kurs, schiene).value" class="icon i-ri-lock-2-line inline-block !opacity-100" />
													<span v-if="allowRegeln && !istKursGesperrtInSchiene(kurs, schiene).value" class="icon i-ri-lock-2-line inline-block !opacity-0 group-hover:!opacity-25" />
												</div>
												<template v-if="showTooltip.kursID === kurs.id && showTooltip.schieneID === schiene.id">
													<svws-ui-tooltip :show-arrow="false" init-open :click-outside="resetDrop">
														<template #content>
															<span class="text-sm-bold">Aktion wählen für Auswahl:</span>
															<svws-ui-button v-if="zusammenKursbezeichnung" size="small" type="transparent" @click="rechteckActions('kurse immer zusammen')">{{ zusammenKursbezeichnung }} immer auf einer Schiene</svws-ui-button>
															<svws-ui-button v-if="zusammenKursbezeichnung" size="small" type="transparent" @click="rechteckActions('kurse nie zusammen')">{{ zusammenKursbezeichnung }} nie auf einer Schiene</svws-ui-button>
															<svws-ui-button size="small" type="transparent" @click="rechteckActions('schienen sperren')">Alle Kurse sperren</svws-ui-button>
															<svws-ui-button size="small" type="transparent" @click="rechteckActions('schienen entsperren')">Alle Kurse entsperren</svws-ui-button>
															<svws-ui-button size="small" type="transparent" @click="rechteckActions('toggle schienen')">Alle Kurse sperren/entsperren</svws-ui-button>
															<svws-ui-button size="small" type="transparent" @click="rechteckActions('kurse fixieren')">Alle Kurse fixieren</svws-ui-button>
															<svws-ui-button size="small" type="transparent" @click="rechteckActions('kurse lösen')">Alle Kurse lösen</svws-ui-button>
															<svws-ui-button size="small" type="transparent" @click="rechteckActions('toggle kurse')">Alle Kurse fixieren/lösen</svws-ui-button>
															<svws-ui-button size="small" type="transparent" @click="rechteckActions('schüler fixieren')">Alle Schüler fixieren</svws-ui-button>
															<svws-ui-button size="small" type="transparent" @click="rechteckActions('schüler lösen')">Alle Schüler lösen</svws-ui-button>
															<svws-ui-button size="small" type="transparent" @click="rechteckActions('toggle schüler')">Alle Schüler fixieren/lösen</svws-ui-button>
															<svws-ui-button size="small" type="transparent" @click="rechteckActions('Schüler AB fixieren')">Alle Schüler in Abiturkursen fixieren</svws-ui-button>
															<svws-ui-button size="small" type="transparent" @click="rechteckActions('Schüler LK fixieren')">Alle Schüler in LK fixieren</svws-ui-button>
															<svws-ui-button size="small" type="transparent" @click="rechteckActions('Schüler LK und AB3 fixieren')">Alle Schüler in LK und dem 3. Abiturfach fixieren</svws-ui-button>
															<svws-ui-button size="small" type="transparent" @click="rechteckActions('Schüler AB3 fixieren')">Alle Schüler im 3. Abiturfach fixieren</svws-ui-button>
															<svws-ui-button size="small" type="transparent" @click="rechteckActions('Schüler AB4 fixieren')">Alle Schüler im 4. Abiturfach fixieren</svws-ui-button>
															<svws-ui-button size="small" type="transparent" @click="rechteckActions('Schüler schriftlichen fixieren')">Alle Schüler im schriftlichen Fächern fixieren</svws-ui-button>
														</template>
													</svws-ui-tooltip>
												</template>
											</div>
										</template>
									</div>
									<!-- Wenn Kurs-Details angewählt sind, erscheint die zusätzliche Zeile -->
									<s-gost-kursplanung-kursansicht-kurs-details v-if="kursdetailAnzeigen === kurs.id && allowRegeln" :bg-color="bgColor(fachwahl)" :anzahl-spalten="6 + getDatenmanager().schieneGetAnzahl()"
										:kurs="kurs" :fachart="GostKursart.getFachartID(kurs.fach_id, kurs.kursart)" :get-datenmanager="getDatenmanager" :api-status="apiStatus"
										:get-ergebnismanager="getErgebnismanager" :map-lehrer="mapLehrer" :regeln-update="regelnUpdate" :add-lehrer-regel="addLehrerRegel"
										:add-kurs="addKurs" :remove-kurse="removeKurse" :add-kurs-lehrer="addKursLehrer" :remove-kurs-lehrer="removeKursLehrer"
										:add-schiene-kurs="addSchieneKurs" :remove-schiene-kurs="removeSchieneKurs" :split-kurs="splitKurs" :combine-kurs="combineKurs" />
								</template>
							</template>
						</template>
					</template>
				</template>
			</svws-ui-table>
			<s-gost-kursplanung-kursansicht-modal-regel-schienen :get-ergebnis-manager="getErgebnismanager" :regeln-update="regelnUpdate" ref="modalRegelKursartSchienen" />
			<s-gost-kursplanung-kursansicht-modal-combine-kurse :get-datenmanager="getDatenmanager" :combine-kurs="combineKurs" ref="modal_combine_kurse" />
		</div>
		<svws-ui-modal :show="() => toRef((fehlermeldungen.size() > 0) && !fehlerIgnore)" type="danger" size="medium">
			<template #modalTitle>Achtung</template>
			<template #modalContent>
				Es sind Fehler in dieser Blockung aufgetaucht:
				<ul>
					<li v-for="fehler in fehlermeldungen" :key="fehler">{{ fehler }}</li>
				</ul>
			</template>
			<template #modalActions>
				<svws-ui-button type="secondary" @click="fehlerIgnore = true"> Schließen </svws-ui-button>
			</template>
		</svws-ui-modal>
	</div>
</template>

<script setup lang="ts">

	import type { WritableComputedRef } from "vue";
	import { computed, onMounted, ref, toRaw, toRef } from "vue";
	import type { ApiStatus } from "~/components/ApiStatus";
	import type { DataTableColumn } from "@ui";
	import type { GostKursplanungSchuelerFilter } from "./GostKursplanungSchuelerFilter";
	import type { GostBlockungKursLehrer, GostBlockungsdatenManager, GostBlockungsergebnisKurs, GostBlockungsergebnisManager,
		GostFach, GostFaecherManager, GostHalbjahr, GostStatistikFachwahl, JavaSet, LehrerListeEintrag, List, GostBlockungRegelUpdate,
		GostBlockungSchiene, GostBlockungRegel} from "@core";
	import { HashMap2D, GostKursart, GostStatistikFachwahlHalbjahr, HashSet, ZulaessigesFach, GostBlockungKurs, GostBlockungsergebnisSchiene,
		SetUtils, ArrayList, GostKursblockungRegelTyp, DeveloperNotificationException } from "@core";
	import { lehrer_filter } from "~/utils/helfer";

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		getKursauswahl: () => JavaSet<number>,
		getErgebnismanager: () => GostBlockungsergebnisManager;
		regelnUpdate: (update: GostBlockungRegelUpdate) => Promise<void>;
		updateKursSchienenZuordnung: (idKurs: number, idSchieneAlt: number, idSchieneNeu: number) => Promise<boolean>;
		patchSchiene: (data: Partial<GostBlockungSchiene>, id : number) => Promise<void>;
		addSchiene: () => Promise<GostBlockungSchiene | undefined>;
		removeSchiene: (s: GostBlockungSchiene) => Promise<GostBlockungSchiene | undefined>;
		patchKurs: (data: Partial<GostBlockungKurs>, kurs_id: number) => Promise<void>;
		addKurs: (fach_id : number, kursart_id : number) => Promise<GostBlockungKurs | undefined>;
		removeKurse: (ids: Iterable<number>) => Promise<void>;
		combineKurs: (kurs1 : GostBlockungKurs, fach2: GostBlockungKurs | GostBlockungsergebnisKurs | undefined | null) => Promise<void>;
		splitKurs: (kurs: GostBlockungKurs) => Promise<void>;
		addKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<GostBlockungKursLehrer | undefined>;
		removeKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<void>;
		addSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
		removeSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
		ergebnisHochschreiben: () => Promise<void>;
		ergebnisAktivieren: () => Promise<boolean>;
		kurssortierung: WritableComputedRef<'fach' | 'kursart'>;
		existiertSchuljahresabschnitt: boolean;
		hatErgebnis: boolean;
		schuelerFilter: () => GostKursplanungSchuelerFilter;
		faecherManager: GostFaecherManager;
		halbjahr: GostHalbjahr;
		mapLehrer: Map<number, LehrerListeEintrag>;
		mapFachwahlStatistik: () => Map<number, GostStatistikFachwahl>;
		blockungstabelleVisible: boolean;
		toggleBlockungstabelle: () => void;
		zeigeSchienenbezeichnungen: () => boolean;
		setZeigeSchienenbezeichnungen: (value: boolean) => void;
		apiStatus: ApiStatus;
	}>();

	const edit_schienenname = ref<number|undefined>(undefined);
	const fehlermeldungen = computed(() => props.getErgebnismanager().getFehlermeldungen());
	const fehlerIgnore = ref<boolean>(false);

	const kurse = computed(() => (props.kurssortierung.value === 'fach')
		? props.getDatenmanager().kursGetListeSortiertNachFachKursartNummer()
		: props.getDatenmanager().kursGetListeSortiertNachKursartFachNummer())

	const schienen = computed<List<GostBlockungSchiene>>(() => props.getDatenmanager().schieneGetListe());

	const allowRegeln = computed<boolean>(() => (props.getDatenmanager().ergebnisGetListeSortiertNachBewertung().size() === 1));

	const cols = computed<DataTableColumn[]>(() => {
		const cols: DataTableColumn[] = [];
		cols.push({ key: "auswahl", label: "Kursauswahl", fixedWidth: 1.5, align: 'center' });
		if (allowRegeln.value)
			cols.push({ key: "actions", label: "Actions", fixedWidth: 1.5, align: 'center' });
		cols.push({ key: "kurs", label: "Kurs", span: 1.75, minWidth: 8 },
			{ key: "lehrer", label: "Lehrer", span: 1.5, minWidth: 6 },
			{ key: "koop", label: "Kooperation", align: 'center', fixedWidth: 3.75 },
			{ key: "FW", label: "Fachwahl", align: 'center', fixedWidth: 3.75 },
			{ key: "Diff", label: "Diff", align: 'center', fixedWidth: 3.75 });
		for (let i = 0; i < schienen.value.size(); i++)
			cols.push({ key: "schiene_" + (i+1), label: "schiene_" + (i+1), fixedWidth: 3.75, align: 'center' });
		return cols;
	});

	const kursdetailAnzeigen = ref<number | undefined>(undefined);
	const setKursdetailAnzeigen = (value: number|undefined) => kursdetailAnzeigen.value = kursdetailAnzeigen.value === value ? undefined : value;

	function getAnzahlSchuelerSchiene(idSchiene: number): number {
		return props.hatErgebnis ? props.getErgebnismanager().getOfSchieneAnzahlSchueler(idSchiene) : 0;
	}

	const fachwahlListe = computed<List<{ kursart : GostKursart, fachwahlen : GostStatistikFachwahl }>>(() => {
		const result = new ArrayList<{ kursart : GostKursart, fachwahlen : GostStatistikFachwahl }>();
		if (props.kurssortierung.value === 'fach') {
			for (const fachwahlen of props.mapFachwahlStatistik().values())
				for (const kursart of GostKursart.values())
					result.add({ kursart, fachwahlen });
		} else {
			for (const kursart of GostKursart.values()) {
				for (const fachwahlen of props.mapFachwahlStatistik().values()) {
					if (kursart === GostKursart.GK) {
						result.add({ kursart, fachwahlen });
						result.add({ kursart : GostKursart.ZK, fachwahlen });
					} else if (kursart !== GostKursart.ZK) {
						result.add({ kursart, fachwahlen });
					}
				}
			}
		}
		return result;
	});

	const fachwahlenAnzahl = computed<HashMap2D<number, number, number>>(() => {
		const result = new HashMap2D<number, number, number>();
		for (const fachwahlen of props.mapFachwahlStatistik().values()) {
			for (const kursart of GostKursart.values()) {
				let anzahl = 0;
				const fach_halbjahr : GostStatistikFachwahlHalbjahr = fachwahlen.fachwahlen[props.halbjahr.id] || new GostStatistikFachwahlHalbjahr();
				const gostfach : GostFach | null = props.faecherManager.get(fachwahlen.id);
				if (gostfach !== null) {
					const zulFach : ZulaessigesFach = ZulaessigesFach.getByKuerzelASD(gostfach.kuerzel);
					switch (kursart) {
						case GostKursart.LK: { anzahl = fach_halbjahr.wahlenLK; break; }
						case GostKursart.GK: { anzahl = (zulFach === ZulaessigesFach.PX) || (zulFach === ZulaessigesFach.VX) ? 0 : fach_halbjahr.wahlenGK; break; }
						case GostKursart.ZK: { anzahl = fach_halbjahr.wahlenZK; break; }
						case GostKursart.PJK: { anzahl = (zulFach === ZulaessigesFach.PX) ? fach_halbjahr.wahlenGK : 0; break; }
						case GostKursart.VTF: { anzahl = (zulFach === ZulaessigesFach.VX) ? fach_halbjahr.wahlenGK : 0; break; }
					}
				}
				result.put(fachwahlen.id, kursart.id, anzahl);
			}
		}
		return result;
	});

	function getAnzahlFachwahlen(fachwahl : { fachwahlen: GostStatistikFachwahl, kursart: GostKursart }) : number {
		const anzahl = fachwahlenAnzahl.value.getOrNull(fachwahl.fachwahlen.id, fachwahl.kursart.id);
		return (anzahl === null) ? 0 : anzahl;
	}

	function istFachwahlVorhanden(fachwahlen: GostStatistikFachwahl, kursart: GostKursart) : boolean {
		const anzahl = props.getDatenmanager().kursGetListeByFachUndKursart(fachwahlen.id, kursart.id).size();
		return (anzahl > 0) || (allowRegeln.value && getAnzahlFachwahlen({ fachwahlen, kursart }) > 0);
	}

	function allow_del_schiene(schiene: GostBlockungSchiene): boolean {
		if (!props.hatErgebnis)
			return false;
		return props.getDatenmanager().schieneGetIsRemoveAllowed(schiene.id) && props.getErgebnismanager().getOfSchieneRemoveAllowed(schiene.id);
	}

	function updateKursauswahl() {
		const auswahl = props.getKursauswahl();
		const allSelected = (props.getDatenmanager().kursGetAnzahl() === auswahl.size());
		if (allSelected)
			auswahl.clear();
		else
			for (const kurs of props.getDatenmanager().kursGetListeSortiertNachFachKursartNummer())
				auswahl.add(kurs.id);
	}

	function getAnzahlKollisionenSchiene(idSchiene: number): number {
		return props.hatErgebnis ? props.getErgebnismanager().getOfSchieneAnzahlSchuelerMitKollisionen(idSchiene) : 0;
	}

	async function patch_schiene(schiene: GostBlockungSchiene, bezeichnung: string) {
		await props.patchSchiene({ bezeichnung }, schiene.id);
		edit_schienenname.value = undefined;
	}

	async function add_schiene() {
		if (!props.apiStatus.pending)
			return await props.addSchiene();
	}

	async function del_schiene(schiene: GostBlockungSchiene) {
		if (!props.apiStatus.pending)
			return await props.removeSchiene(schiene);
	}

	const isMounted = ref(false);
	onMounted(() => isMounted.value = true);

	const dragSperreSchiene = ref<GostBlockungSchiene | undefined>(undefined);

	const modalRegelKursartSchienen = ref();

	function istDropZoneSchiene(schiene: GostBlockungSchiene) : boolean {
		return (dragSperreSchiene.value !== undefined && (dragSperreSchiene.value.id !== schiene.id));
	}

	function openModalRegelKursartSchiene(schiene: GostBlockungSchiene) {
		const andereSchiene = (dragSperreSchiene.value === undefined) ? schiene : dragSperreSchiene.value;
		modalRegelKursartSchienen.value.openModal(andereSchiene, schiene);
	}

	function dragSchieneStarted(schiene: GostBlockungSchiene) {
		dragSperreSchiene.value = schiene;
	}

	function dragSchieneEnded() {
		dragSperreSchiene.value = undefined;
	}

	const modal_combine_kurse = ref();

	/**
	 *
	 * Dieser Teil organisiert das Drag und Drop von Kursen und Schienen
	 *
	 **/

	const dragKurs = ref<GostBlockungKurs|null>(null);
	const dragSchiene  = ref<GostBlockungSchiene|null>(null);
	const dragOverKurs = ref<GostBlockungKurs|null>(null);
	const dragOverSchiene = ref<GostBlockungSchiene|null>(null);
	const dropKurs  = ref<GostBlockungKurs|null>(null);
	const dropSchiene  = ref<GostBlockungSchiene|null>(null);
	const dropKurs2 = ref<GostBlockungKurs|null>(null);
	const dropSchiene2 = ref<GostBlockungSchiene|null>(null);
	const showTooltip = ref<{kursID: number; schieneID: number;}>({kursID: -1, schieneID: -1});
	const kurseUndSchienenInRechteck = ref<[JavaSet<number>, JavaSet<number>, JavaSet<number>|null] | null>(null);

	/** ist das Drag-Objekt ein Kurs, der auf der Schiene liegt? */
	const isKursDragging = computed(() => {
		if (dragKurs.value === null || dragSchiene.value === null)
			return false;
		return props.getErgebnismanager().getOfKursOfSchieneIstZugeordnet(dragKurs.value.id, dragSchiene.value.id);
	})

	/** Soll ein Kurs verschoben werden, dann befinden wir uns im gleichen Kurs, aber auf einer anderen Schiene */
	const highlightKursVerschieben = (kurs: GostBlockungKurs) => computed<boolean>(() => {
		if (dragKurs.value === null || dragSchiene.value === null || dragOverKurs.value === null || dragOverSchiene.value === null)
			return false;
		// wird ein leeres Feld gezogen, dann ist das kein Grund für ein Highlight
		if (!isKursDragging.value)
			return false;
		// befindet sich der Kurs außerhalb der eigenen Zeile, false
		if (dragKurs.value.id !== dragOverKurs.value.id)
			return false;
		// kurs auf Kurs und gleiche Schiene ist ungültig (auch bei Kultikursen)
		if (dragOverKurs.value.id === dragKurs.value.id && props.getErgebnismanager().getOfKursOfSchieneIstZugeordnet(dragKurs.value.id, dragOverSchiene.value.id))
			return false;
		// kurs auf kurs aber andere Schiene ist ok, Kurs fixierteVerschieben
		if (dragOverKurs.value.id === dragKurs.value.id && dragOverSchiene.value.id !== dragSchiene.value.id && kurs.id === dragOverKurs.value.id)
			return true;
		return false;
	})

	/** Es soll ein Kurs auf einen anderen Kurs gezogen werden, um gemeinsame Sache zu machen */
	const highlightKursAufAnderenKurs = (kurs: GostBlockungKurs, schieneE: GostBlockungsergebnisSchiene) => computed<boolean>(() => {
		if (dragKurs.value === null || dragSchiene.value === null || dragOverSchiene.value === null || dragOverKurs.value === null)
			return false;
		const schiene = props.getErgebnismanager().getSchieneG(schieneE.id);
		// wenn kein Kurs gezogen wird, dann kann auch kein Highlighting stattfinden
		if (!isKursDragging.value)
			return false;
		// kurs auf Kurs ist ungültig
		if (kurs.id === dragKurs.value.id)
			return false;
		if (kurs.id !== dragOverKurs.value.id || schiene.id !== dragOverSchiene.value.id)
			return false;
		// kurs auf anderen kurs ist ok, Kurs mit Kurs Sperren/Fixieren etc
		return props.getErgebnismanager().getOfKursOfSchieneIstZugeordnet(dragOverKurs.value.id, dragOverSchiene.value.id);
	})

	/** Wird ein Rechteck gezogen, so wird ein Feld über mehrere Kurse hinweg bewegt und landet nicht auf einem anderen Kurs */
	const highlightRechteck = (kurs: GostBlockungKurs, schieneE: GostBlockungsergebnisSchiene) => computed<boolean>(() => {
		if (dragKurs.value === null || dragSchiene.value === null || dragOverSchiene.value === null || dragOverKurs.value === null)
			return false;
		const schiene = props.getErgebnismanager().getSchieneG(schieneE.id);
		// ein Kurs in der gleichen Zeile, also wenn Kursauswahl = 1, dann nicht erlauben, weil wir verschieben
		if (isKursDragging.value && kurseInRechteckSet.value.size() === 1)
			return false;
		// wenn ich auf einem Kurs lande, dann will ich kein Rechteck, sondern eine Kurs mit Kurs-Aktion, es sei denn ich bin im gleichen Kurs oder ich habe keinen Kurs gezogen
		// aktuell möchte ich das doch...
		// if (isKursDragging.value && props.getErgebnismanager().getOfKursOfSchieneIstZugeordnet(dragOverKurs.value.id, dragOverSchiene.value.id))
		// 	return false;
		// ist der aktuelle Kurs nicht Teil des Rechtecks, dann nicht highlighten
		if (!kurseInRechteckSet.value.contains(kurs.id))
			return false;
		return ((schiene.nummer <= dragSchiene.value.nummer && schiene.nummer >= dragOverSchiene.value.nummer) || (schiene.nummer >= dragSchiene.value.nummer && schiene.nummer <= dragOverSchiene.value.nummer))
	})

	/** Wird ein Rechteck gezogen, so wird ein Feld über mehrere Kurse hinweg bewegt und landet nicht auf einem anderen Kurs */
	const highlightRechteckDrop = (kurs: GostBlockungKurs, schieneE: GostBlockungsergebnisSchiene) => computed<boolean>(() => {
		if (kurseUndSchienenInRechteck.value === null)
			return false;
		const schiene = props.getErgebnismanager().getSchieneG(schieneE.id);
		const [kurse, schienen] = kurseUndSchienenInRechteck.value;
		return (kurse.contains(kurs.id)) && (schienen.contains(schiene.nummer));
	})

	/** Dieses computed ermittelt ein Set von Kursen, die innerhalb des Rechtecks liegen */
	const kurseInRechteckSet = computed<JavaSet<number>>(() => {
		const range = new HashSet<number>();
		if (dragKurs.value === null || dragOverKurs.value === null)
			return range;
		const k1 = toRaw(dragKurs.value);
		const k2 = toRaw(dragOverKurs.value);
		let goon = false;
		const iK1 = kurse.value.indexOf(k1);
		const iK2 = kurse.value.indexOf(k2);
		const kMin = iK1 <= iK2 ? k1 : k2;
		const kMax = iK2 >= iK1 ? k2 : k1;
		for (const k of kurse.value) {
			if (k.id === kMin.id)
				goon = true;
			if (k.id === kMax.id) {
				range.add(k.id);
				break;
			}
			if (goon)
				range.add(k.id);
		}
		return range;
	})

	const zusammenKursbezeichnung = computed<string|null>(() => {
		if (!kurseUndSchienenInRechteck.value?.[2])
			return null;
		const [id1, id2] = kurseUndSchienenInRechteck.value[2];
		return `${props.getDatenmanager().kursGetName(id1)} und ${props.getDatenmanager().kursGetName(id2)}`;
	})

	const isDragging = computed<boolean>(() => (dragSchiene.value !== null && dropSchiene.value === null) && (allowRegeln.value || isKursDragging.value));

	function setDrag(p1: GostBlockungKurs | GostBlockungsergebnisSchiene, p2?: GostBlockungsergebnisSchiene) {
		dragOverKurs.value = null;
		dragOverSchiene.value = null;
		if (p1 instanceof GostBlockungKurs)
			dragKurs.value = p1;
		else
			dragSchiene.value = props.getErgebnismanager().getSchieneG(p1.id);
		if (p2 instanceof GostBlockungsergebnisSchiene && p1 instanceof GostBlockungKurs)
			dragSchiene.value = props.getErgebnismanager().getSchieneG(p2.id);
		else
			throw new DeveloperNotificationException("Es können keine zwei Schienen übergeben werden");
	}

	function setDragOver(kurs: GostBlockungKurs, schiene: GostBlockungsergebnisSchiene) {
		if (kurs.id === dragOverKurs.value?.id && dragOverSchiene.value?.id === schiene.id)
			return;
		dragOverKurs.value = kurs;
		dragOverSchiene.value = props.getErgebnismanager().getSchieneG(schiene.id);
	}

	async function setDrop(p1: GostBlockungKurs | GostBlockungsergebnisSchiene, p2?: GostBlockungsergebnisSchiene) {
		if (p1 instanceof GostBlockungKurs)
			dropKurs.value = p1;
		else
			dropSchiene.value = props.getErgebnismanager().getSchieneG(p1.id);
		if (p2 instanceof GostBlockungsergebnisSchiene && p1 instanceof GostBlockungKurs)
			dropSchiene.value = props.getErgebnismanager().getSchieneG(p2.id);
		else
			throw new DeveloperNotificationException("Es können keine zwei Schienen übergeben werden");
		if (highlightKursVerschieben(p1).value)
			await setKursVerschieben();
		else if (highlightRechteck(p1, p2).value && allowRegeln.value)
			setRechteck();
	}

	async function setKursVerschieben() {
		if (dropKurs.value === null || dropSchiene.value === null || dragKurs.value === null || dragSchiene.value === null)
			return;
		if (allowRegeln.value && props.getDatenmanager().kursGetHatFixierungInSchiene(dragKurs.value.id, dragSchiene.value.id))
			await props.regelnUpdate(props.getErgebnismanager().regelupdateRemove_02e_KURS_FIXIERE_IN_EINER_SCHIENE(dragKurs.value.id, dragSchiene.value.id));
		await props.updateKursSchienenZuordnung(dragKurs.value.id, dragSchiene.value.id, dropSchiene.value.id);
	}

	function setRechteck() {
		dropKurs2.value = dragKurs.value;
		dropSchiene2.value = dragSchiene.value;
		if (dropKurs.value === null || dropSchiene.value === null || dragKurs.value === null || dragSchiene.value === null)
			return;
		showTooltip.value = { kursID: dropKurs.value.id, schieneID: dropSchiene.value.id };
		const s1 = props.getErgebnismanager().getSchieneG(dropSchiene.value.id);
		const s2 = props.getErgebnismanager().getSchieneG(dragSchiene.value.id);
		const schienenSet = new HashSet<number>();
		for (let i = Math.min(s1.nummer, s2.nummer); (i < Math.max(s1.nummer, s2.nummer) +1); i++)
			schienenSet.add(i);
		const kurseZusammen = props.getErgebnismanager().getOfKursOfSchieneIstZugeordnet(dragKurs.value.id, dragSchiene.value.id) && props.getErgebnismanager().getOfKursOfSchieneIstZugeordnet(dropKurs.value.id, dropSchiene.value.id)
			? SetUtils.create2(dragKurs.value.id, dropKurs.value.id)
			: null
		kurseUndSchienenInRechteck.value = [kurseInRechteckSet.value, schienenSet, kurseZusammen];
	}

	function resetDrag() {
		dragKurs.value = null;
		dragSchiene.value = null;
		dragOverKurs.value = null;
		dragOverSchiene.value = null;
	}

	function resetDrop() {
		dropKurs.value = null;
		dropSchiene.value = null;
		dropKurs2.value = null;
		dropSchiene2.value = null;
		kurseUndSchienenInRechteck.value = null;
		showTooltip.value = {kursID: -1, schieneID: -1};
	}

	async function rechteckActions(action: 'kurse fixieren'| 'kurse lösen' | 'toggle kurse' | 'schienen sperren' | 'schienen entsperren' | 'toggle schienen' | 'schüler fixieren' | 'schüler lösen' | 'toggle schüler' | 'Schüler LK fixieren' | 'Schüler AB3 fixieren' | 'Schüler LK und AB3 fixieren' | 'Schüler AB4 fixieren' | 'Schüler AB fixieren' | 'Schüler schriftlichen fixieren' | 'kurse immer zusammen' | 'kurse nie zusammen') {
		if (kurseUndSchienenInRechteck.value === null)
			return false;
		const [kurse, schienen, set] = kurseUndSchienenInRechteck.value;
		resetDrop();
		const update = (() => {
			switch (action) {
				case 'kurse immer zusammen':
					if (set === null)
						throw new DeveloperNotificationException('Es wurde ein leeres Set mit Kursen für Regel 8 übergeben');
					return props.getErgebnismanager().regelupdateCreate_08_KURS_ZUSAMMEN_MIT_KURS(set);
				case 'kurse nie zusammen':
					if (set === null)
						throw new DeveloperNotificationException('Es wurde ein leeres Set mit Kursen für Regel 7 übergeben');
					return props.getErgebnismanager().regelupdateCreate_07_KURS_VERBIETEN_MIT_KURS(set);
				case 'schüler fixieren':
					return props.getErgebnismanager().regelupdateCreate_04b_SCHUELER_FIXIEREN_IN_DEN_KURSEN(kurse);
				case 'schüler lösen':
					return props.getErgebnismanager().regelupdateRemove_04b_SCHUELER_FIXIEREN_IN_DEN_KURSEN(kurse);
				case 'toggle schüler':
					return props.getErgebnismanager().regelupdateRemove_04d_SCHUELER_FIXIEREN_IN_DEN_KURSEN_TOGGLE(kurse);
				case 'kurse fixieren':
					return props.getErgebnismanager().regelupdateCreate_02_KURS_FIXIERE_IN_SCHIENE_MARKIERT(kurse, schienen);
				case 'kurse lösen':
					return props.getErgebnismanager().regelupdateRemove_02_KURS_FIXIERE_IN_SCHIENE_MARKIERT(kurse, schienen);
				case 'toggle kurse':
					return props.getErgebnismanager().regelupdateCreate_02d_KURS_FIXIERE_IN_SCHIENE_TOGGLE(kurse, schienen);
				case 'schienen sperren':
					return props.getErgebnismanager().regelupdateCreate_03_KURS_SPERRE_IN_SCHIENE(kurse, schienen);
				case 'schienen entsperren':
					return props.getErgebnismanager().regelupdateRemove_03_KURS_SPERRE_IN_SCHIENE(kurse, schienen);
				case 'toggle schienen':
					return props.getErgebnismanager().regelupdateCreate_03b_KURS_SPERRE_IN_SCHIENE_TOGGLE(kurse, schienen);
				case 'Schüler AB fixieren':
					return props.getErgebnismanager().regelupdateCreate_04i_SCHUELER_FIXIEREN_TYP_AB_DER_KURSMENGE(kurse);
				case 'Schüler AB3 fixieren':
					return props.getErgebnismanager().regelupdateCreate_04f_SCHUELER_FIXIEREN_TYP_AB3_DER_KURSMENGE(kurse);
				case 'Schüler AB4 fixieren':
					return props.getErgebnismanager().regelupdateCreate_04h_SCHUELER_FIXIEREN_TYP_AB4_DER_KURSMENGE(kurse);
				case 'Schüler LK fixieren':
					return props.getErgebnismanager().regelupdateCreate_04e_SCHUELER_FIXIEREN_TYP_LK_DER_KURSMENGE(kurse);
				case 'Schüler LK und AB3 fixieren':
					return props.getErgebnismanager().regelupdateCreate_04g_SCHUELER_FIXIEREN_TYP_LK_UND_AB3_DER_KURSMENGE(kurse);
				case 'Schüler schriftlichen fixieren':
					return props.getErgebnismanager().regelupdateCreate_04j_SCHUELER_FIXIEREN_TYP_SCHRIFTLICH_DER_KURSMENGE(kurse);
			}
		})();
		await props.regelnUpdate(update);
	}


	//
	// Unterstützung der Anzeige der einzelnen Kurse bzw. Fachwahlen in der Tabelle
	//

	const editKursID = ref<number | undefined>(undefined);

	function listeDerKurse(fachwahl : { fachwahlen: GostStatistikFachwahl, kursart: GostKursart }) : List<GostBlockungKurs> {
		const liste = listenDerKurse.value.getOrNull(fachwahl.fachwahlen.id, fachwahl.kursart.id);
		return (liste === null) ? new ArrayList() : liste;
	}

	const listenDerKurse = computed<HashMap2D<number, number, List<GostBlockungKurs>>>(() => {
		const result = new HashMap2D<number, number, List<GostBlockungKurs>>();
		for (const fachwahlen of props.mapFachwahlStatistik().values())
			for (const kursart of GostKursart.values())
				result.put(fachwahlen.id, kursart.id, props.getDatenmanager().kursGetListeByFachUndKursart(fachwahlen.id, kursart.id));
		return result;
	});

	function bgColor(fachwahl: { fachwahlen: GostStatistikFachwahl, kursart: GostKursart }) : string {
		return ZulaessigesFach.getByKuerzelASD(fachwahl.fachwahlen.kuerzelStatistik).getHMTLFarbeRGBA(1.0);
	}

	function toggleSchuelerFilterFachwahl(fachwahl: { fachwahlen: GostStatistikFachwahl, kursart: GostKursart }) {
		const filter = props.schuelerFilter();
		if (filter === undefined)
			return;
		if (filter.fach !== fachwahl.fachwahlen.id) {
			filter.kursart = fachwahl.kursart;
			filter.fach = fachwahl.fachwahlen.id;
		} else
			filter.reset();
	}

	async function add_kurs(fachwahl: { fachwahlen: GostStatistikFachwahl, kursart: GostKursart }) {
		await props.addKurs(fachwahl.fachwahlen.id, fachwahl.kursart.id);
	}

	async function editKursOnBlur(suffix: string, id: number) {
		await props.patchKurs({ suffix }, id);
		editKursID.value = undefined;
	}

	const kurslehrer = (kurs: GostBlockungKurs) => computed<LehrerListeEintrag | undefined>({
		get: () => {
			const liste = props.getDatenmanager().kursGetLehrkraefteSortiert(kurs.id);
			return liste.size() > 0 ? props.mapLehrer.get(liste.get(0).id) : undefined;
		},
		set: (value) => void setKurslehrer(kurs, value ?? undefined)
	});

	const kurslehrer_liste = (kurs: GostBlockungKurs) => computed<LehrerListeEintrag[]>(() => {
		const vergeben = new Set();
		for (const l of props.getDatenmanager().kursGetLehrkraefteSortiert(kurs.id))
			vergeben.add(l.id);
		const id = kurslehrer(kurs).value?.id;
		if (id)
			vergeben.delete(id);
		const result = [];
		for (const l of props.mapLehrer.values())
			if ((!vergeben.has(l.id)) && (l.istSichtbar))
				result.push(l);
		return result;
	})

	async function setKurslehrer(kurs: GostBlockungKurs, value: LehrerListeEintrag | undefined) {
		const lehrer = kurslehrer(kurs).value
		if ((value === undefined && lehrer === undefined) || (value !== undefined && props.getDatenmanager().kursGetLehrkraftMitIDExists(kurs.id, value.id)))
			return;
		if (value !== undefined) {
			await props.addKursLehrer(kurs.id, value.id);
			await addLehrerRegel();
		}
		if (lehrer !== undefined)
			await props.removeKursLehrer(kurs.id, lehrer.id);
	}

	const lehrer_regel = computed<GostBlockungRegel | undefined>(()=> {
		const regel_typ = GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN;
		const regeln = props.getDatenmanager().regelGetListeOfTyp(regel_typ);
		if (regeln.isEmpty())
			return undefined;
		return regeln.get(0);
	})

	async function addLehrerRegel() {
		if (lehrer_regel.value !== undefined)
			return;
		const update = props.getErgebnismanager().regelupdateCreate_10_LEHRKRAEFTE_BEACHTEN(true);
		await props.regelnUpdate(update);
	}

	function toggle_active_fachwahl(kurs: GostBlockungKurs) {
		const filter = props.schuelerFilter();
		if (filter === undefined)
			return;
		if (filter.fach !== kurs.fach_id || filter.kursart?.id !== kurs.kursart) {
			filter.kursart = GostKursart.fromID(kurs.kursart);
			filter.fach = kurs.fach_id;
		}
		else
			filter.reset();
	}

	async function setKoop(kurs: GostBlockungKurs, istKoopKurs: boolean) {
		await props.patchKurs({ istKoopKurs }, kurs.id);
	}

	const kurs_blockungsergebnis = (kurs: GostBlockungKurs) => computed<GostBlockungsergebnisKurs | undefined>(() => {
		return props.hatErgebnis ? props.getErgebnismanager().getKursE(kurs.id) : undefined;
	});

	const filtered_by_kursart = (kurs: GostBlockungKurs) => computed<List<GostBlockungsergebnisKurs>>(() => {
		const fachart_id = GostKursart.getFachartID(kurs.fach_id, kurs.kursart);
		return props.getErgebnismanager().getOfFachartKursmenge(fachart_id);
	})

	const setze_kursdifferenz = (kurs: GostBlockungKurs) => computed<boolean>(() => {
		return filtered_by_kursart(kurs).value.get(0) === kurs_blockungsergebnis(kurs).value;
	});

	const kursdifferenz = (kurs: GostBlockungKurs) => computed<[number, number, number]>(() => {
		if (filtered_by_kursart(kurs).value.isEmpty())
			return [-1,-1, -1];
		const fachart_id = GostKursart.getFachartID(kurs.fach_id, kurs.kursart);
		const wahlen = props.getDatenmanager().fachwahlGetListeOfFachart(fachart_id).size() || 0;
		const kdiff = props.getErgebnismanager().getOfFachartKursdifferenz(fachart_id);
		return [filtered_by_kursart(kurs).value.size(), kdiff, wahlen];
	});

	const istZugeordnetKursSchiene = (kurs: GostBlockungKurs, schiene: GostBlockungsergebnisSchiene) => computed<boolean>(() => {
		return props.getErgebnismanager().getOfKursOfSchieneIstZugeordnet(kurs.id, schiene.id);
	})

	const istKursFixiertInSchiene = (kurs: GostBlockungKurs, schiene: GostBlockungsergebnisSchiene) => computed<boolean>(() => {
		return props.getDatenmanager().kursGetHatFixierungInSchiene(kurs.id, schiene.id);
	})

	const istKursAusgewaehlt = (kurs: GostBlockungKurs) => computed<boolean>(() => {
		const k = props.getErgebnismanager().getKursE(kurs.id);
		const filter_kurs_id = props.schuelerFilter().kurs?.id;
		return (k !== undefined) && (k.id === filter_kurs_id);
	});

	function toggleKursAusgewaehlt(kurs : GostBlockungKurs) {
		const filter = props.schuelerFilter();
		if (filter === undefined)
			return;
		if (filter.kurs?.id !== kurs.id)
			filter.kurs = kurs;
		else
			filter.reset();
	}

	const istKursGesperrtInSchiene = (kurs: GostBlockungKurs, schiene: GostBlockungsergebnisSchiene) => computed<boolean>(() => {
		return props.getDatenmanager().kursGetHatSperrungInSchiene(kurs.id, schiene.id);
	})

	async function toggleRegelSperreKursInSchiene(kurs: GostBlockungKurs, schiene: GostBlockungsergebnisSchiene) {
		if (!allowRegeln.value)
			return;
		const s = props.getErgebnismanager().getSchieneG(schiene.id);
		const update = (props.getDatenmanager().kursGetHatSperrungInSchiene(kurs.id, schiene.id))
			? props.getErgebnismanager().regelupdateRemove_03_KURS_SPERRE_IN_SCHIENE(SetUtils.create1(kurs.id), SetUtils.create1(s.nummer))
			: props.getErgebnismanager().regelupdateCreate_03_KURS_SPERRE_IN_SCHIENE(SetUtils.create1(kurs.id), SetUtils.create1(s.nummer))
		await props.regelnUpdate(update);
	}

	const istKursVerbotenInSchiene = (kurs: GostBlockungKurs, schiene: GostBlockungsergebnisSchiene) => computed<boolean>(() => {
		return props.getDatenmanager().kursGetIstVerbotenInSchiene(kurs.id, schiene.id);
	})

	async function toggleRegelFixiereKursInSchiene(kurs: GostBlockungKurs, schiene: GostBlockungsergebnisSchiene) {
		if (!allowRegeln.value)
			return;
		const s = props.getErgebnismanager().getSchieneG(schiene.id);
		const update = (props.getDatenmanager().kursGetHatFixierungInSchiene(kurs.id, schiene.id))
			? props.getErgebnismanager().regelupdateRemove_02e_KURS_FIXIERE_IN_EINER_SCHIENE(kurs.id, s.nummer)
			: props.getErgebnismanager().regelupdateCreate_02e_KURS_FIXIERE_IN_EINER_SCHIENE(kurs.id, s.nummer)
		await props.regelnUpdate(update);
	}

</script>

<style lang="postcss" scoped>

	.svws-expanded + .svws-ui-tr:not(.svws-expanded) {
		@apply border-t border-black/50;
	}

</style>
