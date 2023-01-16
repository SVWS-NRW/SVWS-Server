<template>        
    <svws-ui-modal  ref="modalNeueBenutzergruppe"  size="small" >
        <template #modalTitle>
            Benutzergruppe hinzufügen
        </template>
        
        <template #modalContent>
            <div class="content-wrapper">
                <div class="input-wrapper">
                    <svws-ui-text-input v-model="bezeichnung" type="text" placeholder="Bezeichnung" />
                    <svws-ui-checkbox v-model="inputbgIstAdmin"> Admin ? </svws-ui-checkbox>
                </div>
            </div>	
        </template>
        
        <template #modalActions>
            <svws-ui-button type="secondary" @click="modalNeueBenutzergruppe.closeModal()"> Abbrechen </svws-ui-button>
            <svws-ui-button @click="createBenutzergruppe"> Weiter </svws-ui-button>
        </template>
    </svws-ui-modal>
    
    <button class="button button--icon" @click="modalNeueBenutzergruppe.openModal()">
        <svws-ui-icon><i-ri-add-line /></svws-ui-icon>
    </button>
    
    <button  class="button button--icon" v-if="show_delete_icon" @click="deleteBenutzergruppe_n()">
        <svws-ui-icon><i-ri-delete-bin-2-line /></svws-ui-icon>
    </button>
    
    <button class="button button--icon">
        <svws-ui-icon><i-ri-file-copy-line /></svws-ui-icon>
    </button>
    
    <button class="button button--icon">
        <svws-ui-icon><i-ri-more-2-line /></svws-ui-icon>
    </button>
</template>

<script setup lang="ts">
    import { ref } from "vue";
    import { injectMainApp, Main } from "~/apps/Main";

    const main: Main = injectMainApp();
    const modalNeueBenutzergruppe = ref();

    const bezeichnung = ref();
    const inputbgIstAdmin=ref(false);

    const props=defineProps({
        show_delete_icon : {type:Boolean, default:false}
    });

    function createBenutzergruppe(){
            console.log(bezeichnung.value+"-"+inputbgIstAdmin.value)
            main.apps.benutzergruppe.dataBenutzergruppe.create(bezeichnung.value,inputbgIstAdmin.value);
            modalNeueBenutzergruppe.value.closeModal();
            bezeichnung.value="";
            inputbgIstAdmin.value=false;
    }

    function deleteBenutzergruppe_n(){
        main.apps.benutzergruppe.dataBenutzergruppe.deleteBenutzergruppe_n();
    }
</script>
