<template>
	<svws-ui-content-card
		title="Vor der Aufnahme besuchte Schule/Einrichtung/Herkunftsarten"
	>
		<div class="input-wrapper">
			<svws-ui-text-input
				v-model="inputVorigeSchulnummer"
				type="text"
				placeholder="Name der Schule"
			/>
			<svws-ui-text-input
				v-model="inputVorigeAllgHerkunft"
				type="text"
				placeholder="allgemeine Herkunft"
			/>
			<div class="input-wrapper-3-cols">
				<svws-ui-text-input
					v-model="inputVorigeEntlassDatum"
					type="date"
					placeholder="Entlassen am"
				/>
				<svws-ui-text-input
					v-model="inputVorigeEntlassjahrgang"
					type="year"
					placeholder="Entlassjahrgang"
				/>
				<svws-ui-text-input
					v-model="inputVorigeArtLetzteVersetzung"
					type="text"
					placeholder="Versetzung"
				/>
			</div>
			<div class="input-wrapper-3-cols">
				<svws-ui-text-input
					v-model="inputVorigeBemerkung"
					type="text"
					placeholder="Bemerkung"
				/>
				<svws-ui-text-input
					v-model="inputVorigeEntlassgrundID"
					type="text"
					placeholder="Entlassgrund"
				/>
				<svws-ui-text-input
					v-model="inputVorigeAbschlussartID"
					type="text"
					placeholder="höchster allg.-bild. Abschluss"
				/>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.schueler;
	const inputVorigeSchulnummer = computed({
		get(): string {
			if (app && app.schulbesuchsdaten.daten) {
				return String(app.schulbesuchsdaten.daten.vorigeSchulnummer);
			}
			return "";
		},
		set(val: string) {
			val;
			// TODO Server-Implementierung: this.app.schulbesuchsdaten.patch({ vorigeSchulnummer:  val });
		}
	});
	const inputVorigeAllgHerkunft = computed({
		get(): string {
			return String(app.schulbesuchsdaten.daten?.vorigeAllgHerkunft);
		},
		set(val: string) {
			val;
			// TODO Server-Implementierung: this.app.schulbesuchsdaten.patch({ vorigeAllgHerkunft:  val });
		}
	});
	const inputVorigeEntlassDatum = computed({
		get(): string {
			return String(app.schulbesuchsdaten.daten?.vorigeEntlassdatum);
		},
		set(val: string) {
			val;
			// TODO Server-Implementierung: this.app.schulbesuchsdaten.patch({ vorigeEntlassdatum:  val });
		}
	});
	const inputVorigeEntlassjahrgang = computed({
		get(): string {
			return String(app.schulbesuchsdaten.daten?.vorigeEntlassjahrgang);
		},
		set(val: string) {
			val;
			// TODO Server-Implementierung: this.app.schulbesuchsdaten.patch({ vorigeEntlassjahrgang:  val });
		}
	});
	const inputVorigeArtLetzteVersetzung = computed({
		get(): string {
			return String(
				app.schulbesuchsdaten.daten?.vorigeArtLetzteVersetzung
			);
		},
		set(val: string) {
			val;
			// TODO Server-Implementierung: this.app.schulbesuchsdaten.patch({ vorigeArtLetzteVersetzung:  val });
		}
	});
	const inputVorigeBemerkung = computed({
		get(): string {
			return String(app.schulbesuchsdaten.daten?.vorigeBemerkung);
		},
		set(val: string) {
			val;
			// TODO Server-Implementierung: this.app.schulbesuchsdaten.patch({ vorigeBemerkung:  val });
		}
	});
	const inputVorigeEntlassgrundID = computed({
		get(): string {
			return String(app.schulbesuchsdaten.daten?.vorigeEntlassgrundID);
		},
		set(val: string) {
			val;
			// TODO Server-Implementierung: this.app.schulbesuchsdaten.patch({ vorigeEntlassgrundID:  val });
		}
	});
	const inputVorigeAbschlussartID = computed({
		get(): string {
			return String(app.schulbesuchsdaten.daten?.vorigeAbschlussartID);
		},
		set(val: string) {
			val;
			// TODO Server-Implementierung: this.app.schulbesuchsdaten.patch({ vorigeAbschlussartID:  val });
		}
	});
</script>
