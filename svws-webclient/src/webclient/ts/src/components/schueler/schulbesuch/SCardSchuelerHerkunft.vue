<template>
	<svws-ui-content-card title="Vor der Aufnahme besuchte Schule/Einrichtung/Herkunftsarten">
		<div class="input-wrapper">
			<svws-ui-text-input placeholder="Name der Schule" v-model="inputVorigeSchulnummer" type="text" />
			<svws-ui-text-input placeholder="allgemeine Herkunft" v-model="inputVorigeAllgHerkunft" type="text" />
			<div class="input-wrapper-3-cols">
				<svws-ui-text-input placeholder="Entlassen am" v-model="inputVorigeEntlassDatum" type="date" />
				<svws-ui-text-input placeholder="Entlassjahrgang" v-model="inputVorigeEntlassjahrgang" type="year" />
				<svws-ui-text-input placeholder="Versetzung" v-model="inputVorigeArtLetzteVersetzung" type="text" />
			</div>
			<div class="input-wrapper-3-cols">
				<svws-ui-text-input placeholder="Bemerkung" v-model="inputVorigeBemerkung" type="text" />
				<svws-ui-text-input placeholder="Entlassgrund" v-model="inputVorigeEntlassgrundID" type="text" />
				<svws-ui-text-input placeholder="höchster allg.-bild. Abschluss" v-model="inputVorigeAbschlussartID" type="text" />
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
			if (app && app.schulbesuchsdaten.daten)
				return String(app.schulbesuchsdaten.daten.vorigeSchulnummer);
			return "";
		},
		set(val: string) {
			val;
			// TODO Server-Implementierung: this.app.schulbesuchsdaten.patch({ vorigeSchulnummer:  val });
		}
	});

	const inputVorigeAllgHerkunft = computed({
		get(): string | null {
			if (app.schulbesuchsdaten.daten?.vorigeAllgHerkunft !== null)
				return String(app.schulbesuchsdaten.daten?.vorigeAllgHerkunft);
			return null;
		},
		set(val: string | null) {
			val;
			// TODO Server-Implementierung: this.app.schulbesuchsdaten.patch({ vorigeAllgHerkunft:  val });
		}
	});
	const inputVorigeEntlassDatum = computed({
		get(): string | null {
			if (app.schulbesuchsdaten.daten?.vorigeEntlassdatum !== null)
				return String(app.schulbesuchsdaten.daten?.vorigeEntlassdatum);
			return null;
		},
		set(val: string | null) {
			val;
			// TODO Server-Implementierung: this.app.schulbesuchsdaten.patch({ vorigeEntlassdatum:  val });
		}
	});
	const inputVorigeEntlassjahrgang = computed({
		get(): string | null {
			if (app.schulbesuchsdaten.daten?.vorigeEntlassjahrgang !== null)
				return String(app.schulbesuchsdaten.daten?.vorigeEntlassjahrgang);
			return null;
		},
		set(val: string | null) {
			val;
			// TODO Server-Implementierung: this.app.schulbesuchsdaten.patch({ vorigeEntlassjahrgang:  val });
		}
	});

	const inputVorigeArtLetzteVersetzung = computed({
		get(): string | null {
			if (app.schulbesuchsdaten.daten?.vorigeArtLetzteVersetzung !== null)
				return String(app.schulbesuchsdaten.daten?.vorigeArtLetzteVersetzung);
			return null;
		},
		set(val: string | null) {
			val;
			// TODO Server-Implementierung: this.app.schulbesuchsdaten.patch({ vorigeArtLetzteVersetzung:  val });
		}
	});

	const inputVorigeBemerkung = computed({
		get(): string | null {
			if (app.schulbesuchsdaten.daten?.vorigeBemerkung !== null)
				return String(app.schulbesuchsdaten.daten?.vorigeBemerkung);
			return null;
		},
		set(val: string | null) {
			val;
			// TODO Server-Implementierung: this.app.schulbesuchsdaten.patch({ vorigeBemerkung:  val });
		}
	});

	const inputVorigeEntlassgrundID = computed({
		get(): string | null {
			if (app.schulbesuchsdaten.daten?.vorigeEntlassgrundID !== null)
				return String(app.schulbesuchsdaten.daten?.vorigeEntlassgrundID);
			return null;
		},
		set(val: string | null) {
			val;
			// TODO Server-Implementierung: this.app.schulbesuchsdaten.patch({ vorigeEntlassgrundID:  val });
		}
	});

	const inputVorigeAbschlussartID = computed({
		get(): string | null {
			if (app.schulbesuchsdaten.daten?.vorigeAbschlussartID !== null)
				return String(app.schulbesuchsdaten.daten?.vorigeAbschlussartID);
			return null;
		},
		set(val: string | null) {
			val;
			// TODO Server-Implementierung: this.app.schulbesuchsdaten.patch({ vorigeAbschlussartID:  val });
		}
	});

</script>
