<template>
	<div class="card" style="margin-bottom: 3rem">
		<!--		<svws-ui-select title="Versetzung" v-model="vorigeArtLetzteVersetzung" :items="herkunftsarten" :item-text="(h: Herkunftsarten) => getBezeichnung(h) + ' (' + h.daten.kuerzel + ')'" :statistics="showstatistic" class="col-span-full" />-->

		<!-- HIER WIRD GEBAUT -->


		<div class="innerLaylout">
			<div style="width: 8%">
				<span class="icon i-ri-message-line"/>
			</div>


			<div style="width: 80%">
				<div>
					<svws-ui-textarea-input
						v-model="data.Bemerkung"
						:autoresize="true"
						rows="10"
						class="vermerkTextArea"
						@change="
					(newVal) => {
						patching = true;
						patch({ Bemerkung: newVal }, data.id);
					}
				"
					></svws-ui-textarea-input>

					<div class="selectElement">
						<p style="margin-top: auto; margin-bottom: auto; margin-right: 1rem;">Vermerkart:</p>
						<svws-ui-select
							v-model="aktuelleVermerkArt"
							:headless="false"
							:items="[...props.mapVermerkArten.values()]"
							:item-text="
							(item) => {
								return item.bezeichnung;
							}
						"
							@update:modelValue="(newVal: VermerkartEintrag) => { patch({VermerkArt_ID: newVal.id}, data.id)}"
						></svws-ui-select>
					</div>
				</div>
				<p class="profileName">{{ data.AngelegtVon }}</p>

				<div v-if="patching" class="subTextContainer">
					<svws-ui-spinner :spinning="true"/>
				</div>
				<div v-else class="subTextContainer">
					<p v-if="data.GeaendertVon" class="changeProfileName">
						Zuletzt bearbeitet von {{ data.GeaendertVon }} am {{ data.Datum }}
					</p>
					<p v-else class="changeProfileName">Erstellt am {{ data.Datum }} </p>
				</div>


			</div>

			<div style="width: 10%; padding-top: 1rem; display: flex; justify-content: end">
				<span class="trashIcon i-ri-delete-bin-line"></span>

			</div>
		</div>


	</div>
</template>

<script setup lang="ts">
import {
	Herkunftsarten,
	List,
	SchuelerVermerke,
	VermerkartEintrag,
} from "@core";

import {computed, ref} from "vue";
// const emit = defineEmits(['patch'])

const props = defineProps<{
	data: SchuelerVermerke;
	mapVermerkArten: Map<number, VermerkartEintrag>;
	patch: (data: Partial<SchuelerVermerke>, vid: number) => {};
}>();

const patching = ref<boolean>(false);

const aktuelleVermerkArt = computed({
	get: () => {
		patching.value = false;
		return [...props.mapVermerkArten.values()].find((elem) => {
			return elem.id == props.data.VermerkArt_ID;
		});
	},

	set: (newVal) => {
		return newVal;
	},
});
</script>

<style scoped>
.icon {
	width: 4rem;
	height: 4rem;
	display: block;
	margin: auto;

}

.trashIcon {
	width: 2rem;
	height: 2rem;
	margin-left: 1rem;
}

.profileName {
	font-size: 1.5rem;
	padding-left: 1rem;
	margin-top: 1rem;
}

.changeProfileName {
	font-size: 1rem;
	padding-left: 1rem;
}

.card {
	background: rgba(37, 146, 234, 0.21);
	border-radius: 15px;
	padding: 2rem;
}

.innerLaylout {
	display: flex;
	width: 100%;
}

.selectElement {
	padding-left: 1rem;
	margin-top: 0.5rem;
	display: flex;
	width: 50rem;
}

.vermerkTextArea {
	padding: 0rem 1rem;
	font-size: 2rem;
}

.subTextContainer {
	min-height: 2rem;
	margin-bottom: 1rem;
}
</style>
