<template>
	Liste Schüler
	<ul>
		<li v-for="s in liste" :key="s.id" @click="app.auswahl.ausgewaehlt = s">
			{{ s.nachname }}, {{ s.vorname }}
			<div v-if="s === app.auswahl.ausgewaehlt" style="background-color: red;">
				Kommt aus {{schueler?.geburtsort}}
			</div>
		</li>
	</ul>
</template>

<script lang="ts">
	import type {
		SchuelerListeEintrag,
		SchuelerStammdaten
	} from "@svws-nrw/svws-api-ts";
	import { defineComponent } from "vue";
	import { Schueler } from "~/apps/schueler/Schueler";

	export default defineComponent({
		data() {
			return {};
		},
		computed: {
			app(): Schueler {
				return this.$app.apps.schueler;
			},
			liste(): SchuelerListeEintrag[] {
				console.log(this.app)
				return this.app.auswahl.liste;
			},
			schueler(): SchuelerStammdaten | undefined {
				return this.app.stammdaten?.daten;
			}
		},
		mounted() {
			this.app.auswahl.update_list()
		},
		methods: {}
	});
</script>
