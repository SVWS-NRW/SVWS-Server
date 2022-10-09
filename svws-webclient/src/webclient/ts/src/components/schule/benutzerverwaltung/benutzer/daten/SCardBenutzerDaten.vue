<template>
	<svws-ui-content-card title="Daten">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<svws-ui-text-input
					v-model="anzeigename"
					type="text"
					placeholder="Login-Name"
				/>
				<svws-ui-text-input
					v-model="name"
					type="text"
					placeholder="Name"
				/>
				<!-- 
				<svws-ui-text-input
					v-model="inputTextzeugnis"
					type="text"
					placeholder="Zeugnisbezeichnung"
				/> -->
			</div>
		</div>
	</svws-ui-content-card>
	<svws-ui-content-card title="Daten">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<svws-ui-text-input
					v-model="name"
					type="text"
					placeholder="Name"
				/>
				<!-- 
				<svws-ui-text-input
					v-model="inputTextzeugnis"
					type="text"
					placeholder="Zeugnisbezeichnung"
				/> -->
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed, ComputedRef, WritableComputedRef } from "vue";

	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.benutzer;

	const anzeigename: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return app.dataBenutzer.daten?.anzeigename.toString();
		},
		set(val: string | undefined) {
			console.log(val);
			//app.dataReligion.patch({ kuerzel: val });
		}
	});

	const name: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return app.dataBenutzer.daten?.name.toString();
		},
		set(val: string | undefined) {
			console.log(val);
			//app.dataReligion.patch({ kuerzel: val });
		}
	});

	// const inputText: WritableComputedRef<string | undefined> = computed({
	// 	get(): string | undefined {
	// 		return app.dataReligion.daten?.text?.toString();
	// 	},
	// 	set(val: string | undefined) {
	// 		app.dataReligion.patch({ text: val });
	// 	}
	// });

	// const inputTextzeugnis: WritableComputedRef<string | undefined> = computed({
	// 	get(): string | undefined {
	// 		return app.dataReligion.daten?.textZeugnis?.toString();
	// 	},
	// 	set(val: string | undefined) {
	// 		app.dataReligion.patch({ textZeugnis: val });
	// 	}
	// });

	// const inputStatistikKuerzel: WritableComputedRef<Religion | undefined> = computed({
	// 	get(): Religion | undefined {
	// 		return Religion.getByKuerzel(
	// 			app.dataReligion.daten?.kuerzel || null
	// 		) || undefined;
	// 	},
	// 	set(val: Religion | undefined) {
	// 		app.dataReligion.patch({
	// 			kuerzel: val?.daten.kuerzel
	// 		});
	// 	}
	// });
</script>
