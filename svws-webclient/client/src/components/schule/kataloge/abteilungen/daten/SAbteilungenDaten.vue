<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input class="contentFocusField" placeholder="Bezeichnung" :model-value="manager().daten().bezeichnung"
					@change="(v : string | null) => patch({ bezeichnung: (v?.trim() !== '') ? v?.slice(0, 50) : '-' })" :max-len="50" :min-len="1"  />
				<svws-ui-text-input placeholder="Raum" :max-len="20" :model-value="manager().daten().raum"
					@change="(v : string | null) => patch({ raum: v?.slice(0, 20) })" />

				<svws-ui-select title="Lehrer" :items="manager().getLehrer().values()" :item-text="v => v.vorname + ' ' + v.nachname"
					:model-value="manager().getLehrer().get(manager().daten().idAbteilungsleiter)"
					@update:model-value="v => patch({ idAbteilungsleiter: v?.id })" />
				<svws-ui-button type="transparent" @click="goToLehrer(manager().daten().idAbteilungsleiter ?? -1)">
					<span class="icon i-ri-link" />Zum Lehrer
				</svws-ui-button>
				<svws-ui-text-input placeholder="Email" type="email" :max-len="100" :model-value="manager().daten().email"
					@change="(v: string | null) => patch({ email: v?.slice(0, 100) })" />
				<svws-ui-text-input placeholder="Durchwahl" type="tel" :max-len="20" :model-value="manager().daten().durchwahl"
					@change="(v: string | null) => patch({ durchwahl: v?.slice(0,20) })" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { AbteilungenDatenProps } from "~/components/schule/kataloge/abteilungen/daten/SAbteilungenDatenProps";

	const props = defineProps<AbteilungenDatenProps>()

</script>
